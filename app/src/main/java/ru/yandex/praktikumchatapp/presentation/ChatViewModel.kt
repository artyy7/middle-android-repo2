package ru.yandex.praktikumchatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()

    private val _chatState = MutableStateFlow(ChatState())

    val chatState = _chatState.asStateFlow()

    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage().collect { response ->

                    _chatState.update { currentState ->
                        val messages = currentState.messages
                        val newMessages = messages + Message.OtherMessage(response)

                        currentState.copy(
                            messages = newMessages,
                            shouldShowKeyboard =
                                currentState.shouldShowKeyboard || messages.isEmpty()
                        )
                    }
                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        _chatState.update { chatState ->
            chatState.copy(
                messages = chatState.messages + Message.MyMessage(
                    messageText
                )
            )
        }
    }
}
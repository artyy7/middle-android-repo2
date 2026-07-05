package ru.yandex.praktikumchatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()

    private val _messages =
        MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _shouldShowKeyboard = MutableStateFlow(false)

    val shouldShowKeyboard = _shouldShowKeyboard.asStateFlow()

    // TODO Задание 4: замените messages и shouldShowKeyboard на state

    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage().collect { response ->
                    if (_messages.value.isEmpty()) {
                        _shouldShowKeyboard.value = true
                    }

                    _messages.update { it + Message.OtherMessage(response) }
                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        _messages.update { it + Message.MyMessage(messageText) }
    }
}
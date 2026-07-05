package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        var delayMs = RETRY_DELAY_MS

        return api.getReply().retryWhen { cause, attempt ->
            if (cause is Exception && attempt < MAX_RETRIES) {
                delay(delayMs)
                delayMs *= 2
                return@retryWhen true
            }

            return@retryWhen false
        }
    }


    companion object {
        const val RETRY_DELAY_MS = 500L
        const val MAX_RETRIES = 5
    }
}
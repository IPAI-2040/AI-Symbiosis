package cn.darkfog.speech.protocol.stt

import java.io.Serializable

data class SpeechEvent(
    val type: EventType,
    val data: Serializable? = null
)

enum class EventType {
    VAD_START,
    VAD_END,
    ASR_WUW,
    ASR_LOCAL,
    ASR_PARTIAL,
    ASR_CLOUD,
    NLU_LOCAL,
    NLU_CLOUD,
    RESPONSE
}
package cn.darkfog.ai.symbiosis.assistant.engine.speech

import cn.darkfog.ai.symbiosis.assistant.engine.SpeechEventType

data class SpeechEvent(
    val type: SpeechEventType,
    val data: Any
)


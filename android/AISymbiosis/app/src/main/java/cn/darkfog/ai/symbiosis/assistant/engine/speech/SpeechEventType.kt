package cn.darkfog.ai.symbiosis.assistant.engine.speech

enum class SpeechEventType {
    VAD_START,
    VAD_END,
    ASR_PARTIAL,
    ASR_LOCAL,
    ASR_CLOUD,
    NLU_LOCAL,
    NLU_CLOUD
}


package cn.darkfog.dialog_manager.model.bean

data class SpeechRecord(
    var timestamp: Long = 0,
    var pcmFile: String? = null,
    var asr: ASR? = null,
    var nlu: NLU? = null
)

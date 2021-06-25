package cn.darkfog.dialog_manager.model.bean

data class NLURule(
    val asr: ASR,
    val nlu: NLU,
    val type: TYPE = TYPE.FIX
)

enum class TYPE {
    FIX,
    CUSTOME
}
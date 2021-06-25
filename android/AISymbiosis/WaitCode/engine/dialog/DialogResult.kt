package cn.darkfog.dialog_manager

data class DialogResult(
    val outFile: String,
    var asrResult: AsrResult? = null,
    val nluResult: NluResult? = null,
    val runnable: Runnable? = null
)
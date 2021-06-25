package cn.darkfog.dialog_manager

abstract class DialogCallback {

    abstract fun onBestAsrResult(result: AsrResult)

    abstract fun onBestNluResult(result: NluResult)

    abstract fun onError(e: Exception)
}
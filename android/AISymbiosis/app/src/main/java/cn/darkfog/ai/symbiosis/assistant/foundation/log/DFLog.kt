package cn.darkfog.ai.symbiosis.assistant.foundation.log

import com.orhanobut.logger.Logger

const val DEFAULT_LOG = "AISymbiosis"

inline fun logD(block: () -> String?) {
    Logger.t(DEFAULT_LOG).d(block.invoke() ?: "")
}

inline fun logD(tag: String, block: () -> String?) {
    Logger.t(tag).d(block.invoke() ?: "")
}

inline fun logE(block: () -> String?) {
    Logger.t(DEFAULT_LOG).e("",block.invoke())
}

inline fun logE(throwable: Throwable, block: (() -> String?)) {
    Logger.t(DEFAULT_LOG).e(throwable, block.invoke() ?: "")
}

inline fun logE(tag: String, throwable: Throwable, block: () -> String?) {
    Logger.t(tag).e(throwable, block.invoke() ?: "")
}

inline fun logI(block: () -> String?) {
    Logger.t(DEFAULT_LOG).i(block.invoke() ?: "")
}

inline fun logI(tag: String, block: () -> String?) {
    Logger.t(tag).i(block.invoke() ?: "")
}


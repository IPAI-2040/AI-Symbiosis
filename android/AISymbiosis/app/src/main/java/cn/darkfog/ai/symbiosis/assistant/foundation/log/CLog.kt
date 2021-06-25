package cn.darkfog.foundation.log

import com.orhanobut.logger.Logger

interface CLog {
    val logTag: String
        get() = javaClass.simpleName
}

inline fun CLog.logD(block: () -> String?) {
    Logger.t(logTag).d(block.invoke() ?: "")
}

inline fun CLog.logD(tag: String, block: () -> String?) {
    Logger.t(tag).d(block.invoke() ?: "")
}

inline fun CLog.logE(block: () -> String?) {
    Logger.t(logTag).e("",block.invoke())
}

inline fun CLog.logE(throwable: Throwable, block: (() -> String?)) {
    Logger.t(logTag).e(throwable, block.invoke() ?: "")
}

inline fun CLog.logE(tag: String, throwable: Throwable, block: () -> String?) {
    Logger.t(tag).e(throwable, block.invoke() ?: "")
}

inline fun CLog.logI(block: () -> String?) {
    Logger.t(logTag).i(block.invoke() ?: "")
}

inline fun CLog.logI(tag: String, block: () -> String?) {
    Logger.t(tag).i(block.invoke() ?: "")
}


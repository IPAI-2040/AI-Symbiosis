package cn.darkfog.foundation.log

import com.orhanobut.logger.Logger

interface DFLog {
    val logTag: String
        get() = javaClass.simpleName
}

inline fun DFLog.logD(block: () -> String?) {
    Logger.t(logTag).d(block.invoke() ?: "")
}

inline fun DFLog.logD(tag: String, block: () -> String?) {
    Logger.t(tag).d(block.invoke() ?: "")
}

inline fun DFLog.logE(block: () -> String?) {
    Logger.t(logTag).e("",block.invoke())
}

inline fun DFLog.logE(throwable: Throwable, block: (() -> String?)) {
    Logger.t(logTag).e(throwable, block.invoke() ?: "")
}

inline fun DFLog.logE(tag: String, throwable: Throwable, block: () -> String?) {
    Logger.t(tag).e(throwable, block.invoke() ?: "")
}

inline fun DFLog.logI(block: () -> String?) {
    Logger.t(logTag).i(block.invoke() ?: "")
}

inline fun DFLog.logI(tag: String, block: () -> String?) {
    Logger.t(tag).i(block.invoke() ?: "")
}


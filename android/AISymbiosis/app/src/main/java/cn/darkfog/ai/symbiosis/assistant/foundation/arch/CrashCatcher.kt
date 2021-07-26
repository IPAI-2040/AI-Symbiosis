package cn.darkfog.ai.symbiosis.assistant.foundation.arch

import cn.darkfog.foundation.log.DFLog
import cn.darkfog.foundation.log.logE


class CrashCatcher : Thread.UncaughtExceptionHandler,
    DFLog {
    override fun uncaughtException(t: Thread, e: Throwable) {
        logE(e) { "$t crash" }
    }
}
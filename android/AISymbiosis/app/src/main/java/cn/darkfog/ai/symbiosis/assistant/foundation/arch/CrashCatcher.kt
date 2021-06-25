package cn.darkfog.ai.symbiosis.assistant.foundation.arch

import cn.darkfog.foundation.log.CLog
import cn.darkfog.foundation.log.logE


class CrashCatcher : Thread.UncaughtExceptionHandler,
    CLog {
    override fun uncaughtException(t: Thread, e: Throwable) {
        logE(e) { "$t crash" }
    }
}
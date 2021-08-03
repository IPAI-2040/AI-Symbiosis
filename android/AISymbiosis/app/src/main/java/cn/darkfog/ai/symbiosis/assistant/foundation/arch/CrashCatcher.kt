package cn.darkfog.ai.symbiosis.assistant.foundation.arch

import cn.darkfog.ai.symbiosis.assistant.foundation.log.logE


class CrashCatcher : Thread.UncaughtExceptionHandler{
    override fun uncaughtException(t: Thread, e: Throwable) {
        logE(e) { "$t crash" }
    }
}
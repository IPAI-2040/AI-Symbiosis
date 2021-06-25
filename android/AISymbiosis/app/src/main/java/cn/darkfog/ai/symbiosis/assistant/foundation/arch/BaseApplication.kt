package cn.darkfog.ai.symbiosis.assistant.foundation.arch

import android.app.Application
import cn.darkfog.ai.symbiosis.assistant.foundation.util.LoggerUtil

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContextLinker.setupLink(this)
        Thread.setDefaultUncaughtExceptionHandler(CrashCatcher())
        LoggerUtil.init("DarkFog")
    }

}
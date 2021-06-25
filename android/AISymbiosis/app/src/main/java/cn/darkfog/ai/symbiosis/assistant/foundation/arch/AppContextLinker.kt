package cn.darkfog.ai.symbiosis.assistant.foundation.arch

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
object AppContextLinker{
    lateinit var context: Context
        private set

    fun setupLink(application: Application){
        context = application
    }

}
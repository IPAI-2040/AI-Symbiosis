package cn.darkfog.ai.symbiosis.assistant.foundation.util

import androidx.annotation.Nullable
import com.orhanobut.logger.*


object LoggerUtil {

    fun init(tag: String,
             enablePrint: Boolean = true,
             enableStorage: Boolean = true
    ) {
        val prettyFormatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(0)
            .tag(tag)
            .build()
        setuoLogger(
            prettyFormatStrategy,
            enablePrint,
            enableStorage
        )
    }

    fun init(enablePrint: Boolean = true,
             enableStorage: Boolean = true
    ) {
        val prettyFormatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(0)
            .tag("")
            .build()

        setuoLogger(
            prettyFormatStrategy,
            enablePrint,
            enableStorage
        )
    }

    private fun setuoLogger(
        formatStrategy: FormatStrategy,
        enablePrint: Boolean,
        enableStorage: Boolean
    ) {


        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, @Nullable tag: String?): Boolean {
                return enablePrint
            }
        })

        Logger.addLogAdapter(object : DiskLogAdapter() {
            override fun isLoggable(priority: Int, @Nullable tag: String?): Boolean {
                return enableStorage
            }
        })
    }
}
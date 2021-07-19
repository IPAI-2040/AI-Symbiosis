package cn.darkfog.ai.symbiosis.assistant.resolver

import com.google.gson.JsonObject
import io.reactivex.Maybe

// TODO: 2021/7/19 支持并行处理，区分android和其他进程

object ContextContainer {

    suspend fun requestContext(action:String):ResolveResult {
        return ResolveResult()
    }



}
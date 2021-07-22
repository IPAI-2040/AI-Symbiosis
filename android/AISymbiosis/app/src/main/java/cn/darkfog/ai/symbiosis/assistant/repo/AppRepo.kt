package cn.darkfog.ai.symbiosis.assistant.repo

import cn.darkfog.ai.symbiosis.context.ExecuteResult
import cn.darkfog.ai.symbiosis.context.RequestResult
import cn.darkfog.ai.symbiosis.context.annotation.Action
import cn.darkfog.ai.symbiosis.context.annotation.Request
import cn.darkfog.ai.symbiosis.context.annotation.Domain
import com.google.gson.JsonObject

@Domain("Apps")
object AppRepo {

    @Request("getAllApps")
    suspend fun getAllApps(frames:Map<String,String>,data:Map<String,MutableList<JsonObject>>):RequestResult{
        return RequestResult()
    }

    @Action("openApp")
    suspend fun openApp(frames:Map<String,String>):ExecuteResult{
        return ExecuteResult()
    }


}
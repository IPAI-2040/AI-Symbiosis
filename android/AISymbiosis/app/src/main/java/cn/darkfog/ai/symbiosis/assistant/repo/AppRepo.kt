package cn.darkfog.ai.symbiosis.assistant.repo

import cn.darkfog.ai.symbiosis.context.annotation.Action
import cn.darkfog.ai.symbiosis.context.annotation.Domain
import com.google.gson.JsonObject

@Domain("Apps")
object AppRepo {

    @Action("getAllApps")
    fun getAllApps(frames:Map<String,String>,data:Map<String,MutableList<JsonObject>>):MutableList<JsonObject>{
        return mutableListOf()
    }


}
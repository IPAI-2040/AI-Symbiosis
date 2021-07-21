package cn.darkfog.ai.symbiosis.assistant.repo

import cn.darkfog.ai.symbiosis.context.annotation.Action
import cn.darkfog.ai.symbiosis.context.annotation.Domain
import com.google.gson.JsonObject

@Domain("Apps")
object AppRepo {

    @Action("getAllApps")
    fun getAllApps():MutableList<JsonObject>{
        return mutableListOf()
    }


}
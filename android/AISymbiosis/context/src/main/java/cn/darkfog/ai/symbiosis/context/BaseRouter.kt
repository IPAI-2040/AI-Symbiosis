package cn.darkfog.ai.symbiosis.context

import com.google.gson.JsonObject

typealias RequestDataFunc = (Map<String,String>,Map<String,MutableList<JsonObject>>) -> MutableList<JsonObject>

open class BaseRouter {
    private val routerMap:MutableMap<String,RequestDataFunc>  = mutableMapOf()

    fun addRequestDataFunc(action:String,func:RequestDataFunc){
        routerMap[action] = func
    }

    fun requestDataByAction(action:String,frames:Map<String,String>,data:Map<String,MutableList<JsonObject>>):MutableList<JsonObject>{
        return routerMap[action]?.invoke(frames,data)?: mutableListOf()
    }

}
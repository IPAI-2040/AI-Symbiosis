package cn.darkfog.ai.symbiosis.context

import com.google.gson.JsonObject

typealias RequestDataFunc = suspend (Map<String,String>,Map<String,MutableList<JsonObject>>) -> RequestResult
typealias ActionHandler = suspend (Map<String,String>) -> ExecuteResult


data class RequestResult(
    val code:RequestCode = RequestCode.NotFound,
    val data:MutableList<JsonObject> = mutableListOf()
)

enum class RequestCode{
    NotFound,
    Success,
    Failed
}

data class ExecuteResult(
    val code:ExecuteCode =ExecuteCode.NotFound,
    val data:MutableList<JsonObject> = mutableListOf()
)

enum class ExecuteCode{
    NotFound,
    Success,
    Failed
}

open class BaseRouter {
    private val requestRouter:MutableMap<String,RequestDataFunc>  = mutableMapOf()
    private val actionRouter:MutableMap<String,ActionHandler>  = mutableMapOf()

    fun addActionHandler(action: String,handler:ActionHandler){
        actionRouter[action] = handler
    }

    fun addRequestDataFunc(action:String,func:RequestDataFunc){
        requestRouter[action] = func
    }

    suspend fun requestData(action:String,frames:Map<String,String>,data:Map<String,MutableList<JsonObject>>):RequestResult{
        return requestRouter[action]?.invoke(frames,data)?: RequestResult()
    }

    suspend fun executeAction(action:String, frames:Map<String,String>):ExecuteResult{
        return actionRouter[action]?.invoke(frames)?:ExecuteResult()
    }

}
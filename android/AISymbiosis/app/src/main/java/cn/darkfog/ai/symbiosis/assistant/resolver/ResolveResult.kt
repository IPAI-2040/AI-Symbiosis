package cn.darkfog.ai.symbiosis.assistant.resolver

import com.google.gson.JsonObject

data class ResolveResult(
    val status:ResolveState = ResolveState.FAIL,
    val data:MutableList<JsonObject> = mutableListOf()
)
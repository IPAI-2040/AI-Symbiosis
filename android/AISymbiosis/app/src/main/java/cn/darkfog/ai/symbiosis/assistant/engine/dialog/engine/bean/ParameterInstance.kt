package cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine.bean

import android.content.ContentValues
import cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean.Parameter
import com.google.gson.JsonObject

class ParameterInstance(val parameterInfo: Parameter) {
    var necessary: Boolean = false
    var status: ParameterStatus = ParameterStatus.Missed
    var input: MutableMap<String, String> = mutableMapOf()
    var list: MutableList<JsonObject> = mutableListOf()
    var selectIndex: Int = 0
}

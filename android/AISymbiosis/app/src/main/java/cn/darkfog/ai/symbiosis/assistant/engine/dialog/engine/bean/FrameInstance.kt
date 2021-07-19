package cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine.bean

import cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean.Frame
import com.google.gson.JsonObject

class FrameInstance(val info: Frame) {
    var necessary: Boolean = false
    var status: FrameStatus = FrameStatus.Missed
    var input: MutableMap<String, String> = mutableMapOf()
    var list: MutableList<JsonObject> = mutableListOf()
    var selectIndex: Int = 0
}

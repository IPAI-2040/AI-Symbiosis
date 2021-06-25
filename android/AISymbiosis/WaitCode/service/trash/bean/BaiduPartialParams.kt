package cn.darkfog.speech_service.trash.bean

data class BaiduPartialParams(
    val best_result: String,
    val result_type: String
)

data class OriginResult(
    val corpus_no: Long,
    val err_no: Int,
    val result: Result,
    val sn: String
)

data class Result(
    val word: List<String>
)
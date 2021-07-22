package cn.darkfog.ai.symbiosis.context.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Request(
    val name:String
)

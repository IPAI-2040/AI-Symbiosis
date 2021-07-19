package cn.darkfog.ai.symbiosis.context.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Action(
    val name:String
)

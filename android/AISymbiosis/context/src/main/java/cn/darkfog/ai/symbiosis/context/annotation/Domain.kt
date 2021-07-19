package cn.darkfog.ai.symbiosis.context.annotation


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Domain(
    val name:String
)
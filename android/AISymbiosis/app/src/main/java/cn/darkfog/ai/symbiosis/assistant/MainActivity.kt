package cn.darkfog.ai.symbiosis.assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu.BaiduWakeUpEngine

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG){
            finish()
        }
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.test_wake).setOnClickListener {
            BaiduWakeUpEngine.init().doOnComplete {
                addShowInfo("init complete")
                BaiduWakeUpEngine.start()
                    .doOnSuccess { addShowInfo("wake up $it") }
                    .doOnError { addShowInfo("wake up error $it") }
                    .doOnComplete { addShowInfo("wake complete") }
                    .subscribe()
            }.doOnError { addShowInfo("init error $it") }.subscribe()
        }
    }



    fun addShowInfo(info:String){
        val textView = findViewById<TextView>(R.id.tv_Info)
        textView.text = textView.text.toString() + "\n" +info
    }

}
package cn.darkfog.ai.symbiosis.assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!BuildConfig.DEBUG){
            finish()
        }
        setContentView(R.layout.activity_main)
    }

}
package cn.darkfog.dialog_manager.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.darkfog.dialog_manager.R
import cn.darkfog.dialog_manager.history.HistoryFragment

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HistoryFragment.newInstance(1))
                .commitNow()
        }
    }
}
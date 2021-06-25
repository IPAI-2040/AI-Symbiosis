package cn.darkfog.dialog_manager.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cn.darkfog.dialog_manager.R
import cn.darkfog.dialog_manager.model.bean.SpeechRecord
//import cn.darkfog.dialog_manager.model.bean.SpeechRecordState

class HistoryRecyclerViewAdapter(
    private val values: List<SpeechRecord>,
    private val callback: (Int, SpeechRecord) -> Unit
) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = values[position]

        holder.asr.text = record.asr?.content
        holder.nlu.text = "Domian:${record.nlu?.domain}; Intent:${record.nlu?.intent}"

        holder.play.setOnClickListener {
            callback(it.id, record)
        }

        holder.confirm.setOnClickListener {
            callback(it.id, record)
        }

        holder.detail.setOnClickListener {
            callback(it.id, record)
        }
    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val play = view.findViewById<ConstraintLayout>(R.id.play)
        val confirm = view.findViewById<ConstraintLayout>(R.id.confirm)


        val asr = view.findViewById<TextView>(R.id.asr)
        val nlu = view.findViewById<TextView>(R.id.nlu)
        val time = view.findViewById<TextView>(R.id.time)

        val detail = view.findViewById<ConstraintLayout>(R.id.detail)
    }

}
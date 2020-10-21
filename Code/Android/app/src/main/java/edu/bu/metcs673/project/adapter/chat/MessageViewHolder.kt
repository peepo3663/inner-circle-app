package edu.bu.metcs673.project.adapter.chat

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bu.metcs673.project.R

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val mTextView = itemView.findViewById<TextView>(R.id.text_message_body)
}

package edu.bu.metcs673.project.adapter.chat

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bu.metcs673.project.R

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val mTextView: TextView = itemView.findViewById(R.id.text_message_body)
    val mTextTime: TextView = itemView.findViewById(R.id.text_message_time)
    val mPictureView: ImageView? = itemView.findViewById(R.id.image_message_profile)
}

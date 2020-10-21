package edu.bu.metcs673.project.adapter.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bu.metcs673.project.model.chat.MessageModel

public class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    private var responseMessages: ArrayList<MessageModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun getItemCount(): Int = responseMessages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.mTextView.text = responseMessages[position].textMessage
    }

}
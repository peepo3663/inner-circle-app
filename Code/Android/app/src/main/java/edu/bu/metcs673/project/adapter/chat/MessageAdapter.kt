package edu.bu.metcs673.project.adapter.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.model.chat.MessageModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    private var responseMessages: List<MessageModel> = ArrayList()

    fun setMessages(messages: List<MessageModel>) {
        this.responseMessages = messages
    }

    override fun getItemViewType(position: Int): Int {
        val message = responseMessages[position]
        return if (message.isOwnMessage == true) {
            R.layout.item_message_user
        } else {
            R.layout.item_message_friend
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }
    override fun getItemCount(): Int = responseMessages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = responseMessages[position]
        holder.mTextView.text = message.text
        val createdDate = message.createdAt?.toDate()
        createdDate?.let {
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            holder.mTextTime.text = simpleDateFormat.format(it)
        }
        if (holder.mPictureView != null && message.isOwnMessage == false) {
            Glide.with(holder.itemView).load(message.profile_picture).circleCrop().into(holder.mPictureView)
        }
    }

}
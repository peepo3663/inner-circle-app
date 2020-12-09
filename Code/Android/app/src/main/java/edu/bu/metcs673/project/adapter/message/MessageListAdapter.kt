package edu.bu.metcs673.project.adapter.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.model.message.ChatRoomModel
import edu.bu.metcs673.project.ui.listener.OnItemClickListener

class MessageListAdapter(val itemClickListener: OnItemClickListener): RecyclerView.Adapter<MessageListViewHolder>() {

    private var allChatRooms: List<ChatRoomModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListViewHolder {
        return MessageListViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun getItemCount(): Int {
        return allChatRooms.size
    }

    override fun onBindViewHolder(holder: MessageListViewHolder, position: Int) {
        val chatRoom = allChatRooms[position]
        holder.bind(chatRoom, itemClickListener)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_chat_room
    }

    fun setChatRoom(chatRooms: List<ChatRoomModel>) {
        this.allChatRooms = chatRooms
    }

}
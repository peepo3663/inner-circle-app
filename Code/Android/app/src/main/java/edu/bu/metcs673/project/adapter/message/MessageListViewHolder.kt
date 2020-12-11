package edu.bu.metcs673.project.adapter.message

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.model.message.ChatRoomModel
import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.ui.listener.OnItemClickListener

class MessageListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val profilePictureView = itemView.findViewById<ImageView>(R.id.icon)
    val chatRoomNameView = itemView.findViewById<TextView>(R.id.name)
    val latestTextView = itemView.findViewById<TextView>(R.id.latest_text)

    fun bind(chatRoomModel: ChatRoomModel, clickListener: OnItemClickListener) {
        itemView.setOnClickListener {
            clickListener.onItemClicked(chatRoomModel)
        }
        latestTextView.text = chatRoomModel.latestText
        if (chatRoomModel.users != null) {
            val users = chatRoomModel.users as ArrayList<User>
            for (user in users) {
                if (user.userId != FirebaseAuth.getInstance().currentUser?.uid) {
                    chatRoomNameView.text = user.name
                    Glide.with(itemView).load(user.profilePicture).circleCrop().into(profilePictureView)
                }
            }
        }
    }
}
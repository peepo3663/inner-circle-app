package edu.bu.metcs673.project.adapter.chat

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.ui.chat.QueriedUserEmailChatFragment
import edu.bu.metcs673.project.ui.listener.OnFriendClickListener
import edu.bu.metcs673.project.ui.listener.OnItemClickListener

//@brief Class implementation of adaptor used for notification fragment
//  By default, we must override three functions: getItemCount, onCreateViewHolder, onBindHolder
class SearchAdapter(var mUsers: List<User>, private val itemClickListener: OnFriendClickListener) :
    RecyclerView.Adapter<CustomViewHolder?>() {

    private lateinit var context: Context

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {
        this.context = viewGroup.context
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.user_search_display, viewGroup, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val userProfile = mUsers[position]
        holder.userEmail.text = userProfile.email
        Glide.with(holder.view).load(userProfile.profilePicture)
            .placeholder(R.drawable.ic_launcher_foreground).circleCrop().into(holder.userImageView)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClicked(userProfile)
        }
    }
}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val userEmail: TextView = itemView.findViewById(R.id.userEmail)
    val userImageView: ImageView = itemView.findViewById(R.id.friendImageView)
}
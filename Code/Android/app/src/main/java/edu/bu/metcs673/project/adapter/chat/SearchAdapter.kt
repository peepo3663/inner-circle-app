package edu.bu.metcs673.project.adapter.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.model.chat.UserEmailModel

//@brief Class implementation of adaptor used for notification fragment
//  By default, we must override three functions: getItemCount, onCreateViewHolder, onBindHolder
class SearchAdapter(private val mContext: Context, private val mUsers: List<UserEmailModel>) :
    RecyclerView.Adapter<CustomViewHolder?>() {

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.user_search_display, viewGroup, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val userprofile = mUsers[position]
        holder.userEmail.text = userprofile.email
    }
}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val userEmail: TextView = itemView.findViewById(R.id.useremail)
}
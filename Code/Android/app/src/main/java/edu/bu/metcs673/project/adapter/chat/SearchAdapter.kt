package edu.bu.metcs673.project.adapter.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.model.chat.UserEmailModel

class SearchAdapter (mContext: Context, mUsers:List<UserEmailModel>): RecyclerView.Adapter<CustomViewHolder?>(){

    private val mContext:Context
    private val mUsers:List<UserEmailModel>

    init {
        this.mContext=mContext
        this.mUsers=mUsers
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {
        val view: View= LayoutInflater.from(mContext).inflate(R.layout.user_search_display,viewGroup)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val userprofile = mUsers[position]
        holder.useremail.text=userprofile.email
    }


}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

    lateinit var useremail: TextView
    init {
        useremail=itemView.findViewById(R.id.useremail)
    }

}
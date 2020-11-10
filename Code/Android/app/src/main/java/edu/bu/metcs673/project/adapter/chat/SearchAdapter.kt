package edu.bu.metcs673.project.adapter.chat

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User

class SearchAdapter (mContext: Context, mUsers: List<User>): RecyclerView.Adapter<CustomViewHolder?>(){

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){


}
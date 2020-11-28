package edu.bu.metcs673.project.adapter.chat

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.model.chat.UserEmailModel
import edu.bu.metcs673.project.ui.chat.ChatFragment
import edu.bu.metcs673.project.ui.chat.QueriedUserEmailChatFragment

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

        holder.itemView.setOnClickListener{
            val options= arrayOf<CharSequence>(
                "Send Message",
                "View Profile"
            )

            val builder: AlertDialog.Builder=AlertDialog.Builder(mContext)
            builder.setTitle("What do you want to do?")
            builder.setItems(options,DialogInterface.OnClickListener{ dialog, position ->
                if (position==0)
                {
                    val intent = Intent(mContext, QueriedUserEmailChatFragment::class.java)
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    intent.putExtra("visit_id",userprofile.uID)

                    //Start activity
                    mContext.startActivity(intent)
                }

            })
            builder.show()


        }
    }
}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val userEmail: TextView = itemView.findViewById(R.id.useremail)
}
package edu.bu.metcs673.project.ui.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.chat.SearchAdapter
import edu.bu.metcs673.project.model.chat.UserEmailModel
import kotlinx.android.synthetic.main.fragment_chat.*

class QueriedUserEmailChatFragment : AppCompatActivity(){

    var sendButton: Button?=null
    var userIdVisit: String=""
    var firebaseUser: FirebaseUser?=null
    private val chatsRef: CollectionReference = Firebase.firestore.collection("chats")
    private lateinit var currentChatId: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat)

        intent=intent
        userIdVisit= intent.getStringExtra("visit_id").toString()
        val firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid

        sendButton = findViewById(R.id.button_chatbox_send)
        sendButton?.setOnClickListener{

            val message=user_text_chatbox.text.toString()

            if (message == "")
            {
                // do nothing if there is no message to send
            }
            else
            {
                sendMessagetoUser(firebaseUserId,userIdVisit,message)
                println("IT WORKS!!")
            }

        }

    }

    private fun sendMessagetoUser(senderId: String?, receiverId: String, message: String) {

        val MessageHashMap = HashMap<String, Any?>()
        MessageHashMap["sender"]=senderId
        MessageHashMap["receiver"]=receiverId
        MessageHashMap["message"]=message

        println("CurrentChatId")

        chatsRef.document("12MCY06Cac6jg5dkOkgJ").collection("messages").add(MessageHashMap)

    }


}
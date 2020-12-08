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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.chat.ChatsAdapter
import edu.bu.metcs673.project.adapter.chat.IndividualChat
import edu.bu.metcs673.project.adapter.chat.SearchAdapter
import edu.bu.metcs673.project.model.chat.MessageModel
import edu.bu.metcs673.project.model.chat.UserEmailModel
import edu.bu.metcs673.project.model.message.ChatRoomModel
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.user_text_chatbox
import kotlinx.android.synthetic.main.fragment_chat_query.*

class QueriedUserEmailChatFragment : AppCompatActivity(){

    //Initialize variables
    var sendButton: Button?=null
    var userIdVisit: String=""
    var username: String=""
    var firebaseUser: FirebaseUser?=null
    private val chatsRef: CollectionReference = Firebase.firestore.collection("chats")
    private lateinit var currentChatId: String
    var chatsAdapter: ChatsAdapter?=null
    var mChatList: List<IndividualChat>?=null
    lateinit var recycler_view_chat: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_query)

        //Find view by ids
        sendButton = findViewById(R.id.button_chatbox_send)
        recycler_view_chat = findViewById(R.id.recyclerViewMessageList)

        //Retrieve intent information
        intent=intent
        userIdVisit= intent.getStringExtra("visit_id").toString()
        username=intent.getStringExtra("username").toString()

        val firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid
        var linearLayoutManager = LinearLayoutManager(applicationContext)

        ReceiverName.text=username

        //Set up recycler view
        recycler_view_chat.setHasFixedSize(true)
        linearLayoutManager.stackFromEnd=true
        recycler_view_chat.layoutManager = linearLayoutManager

        //Place retrieve function into appropriate setting
        retrieveMessages(firebaseUserId,userIdVisit)

        sendButton?.setOnClickListener{

            val message=user_text_chatbox.text.toString()

            if (message == "")
            {
                // do nothing if there is no message to send
            }
            else
            {
                sendMessagetoUser(firebaseUserId,userIdVisit,message)
            }
            user_text_chatbox.setText("")

        }

    }

    private fun retrieveMessages(senderId: String?, receiverId: String)
    {
        mChatList=ArrayList()
        val reference= chatsRef.document("4112T5vcMEnnu7ltmX96").collection("messages")

        reference.addSnapshotListener{ messages, _ ->
            (mChatList as ArrayList<IndividualChat>).clear()
            if (messages != null && !messages.isEmpty)
            {
                for (message in messages.documents)
                {
                    val messageObject = message.toObject(IndividualChat::class.java)
                    if (messageObject != null) {
                        if ((messageObject.getSender().equals(senderId) && messageObject.getReceiver().equals(receiverId)) ||
                                (messageObject.getSender().equals(receiverId) && messageObject.getReceiver().equals(senderId))){
                            (mChatList as ArrayList<IndividualChat>).add(messageObject)
                        }
                    }
                }
                chatsAdapter= ChatsAdapter(this, mChatList as ArrayList<IndividualChat>)
                recycler_view_chat.adapter=chatsAdapter
            }

        }
    }

    private fun sendMessagetoUser(senderId: String?, receiverId: String, message: String) {

        val MessageHashMap = HashMap<String, Any?>()
        MessageHashMap["sender"]=senderId
        MessageHashMap["receiver"]=receiverId
        MessageHashMap["message"]=message


            //val length = 10
            //val uniquechatid = getRandomString(length)

            var chatroomUser1: String=""
            var chatroomUser2: String=""
            val chatroomid=""

/*

            //To do: December 7, 2020
            //Check if the chat exists
            chatsRef.addSnapshotListener{ chatrooms, _ ->

                val documents=chatrooms!!.documents
                documents.forEach{
                    val singlechatroomObject=it.toObject(ChatRoomModel::class.java)
                    chatroomUser1 = singlechatroomObject!!.userIds!![0]
                    chatroomUser2 = singlechatroomObject!!.userIds!![1]

                    // Check whether chatroom exists for the current sender and receiver
                    if ((chatroomUser1.equals(senderId) || chatroomUser1.equals(receiverId)) &&
                            (chatroomUser2.equals(senderId) || chatroomUser2.equals(receiverId)))
                    {
                        //chatsRef.document(chatroomid).collection("messages").add(MessageHashMap)
                        val chatroomid=it.id
                    }
                }

                // If chatroom id not found this user and reciever, create one now
                if (chatroomid=="")
                {
                    // Create a new chat room for these users
                    val newchatroom =  HashMap<String, Any?>()
                    var userIDarray= listOf(senderId,receiverId)

                    newchatroom["userIds"]=userIDarray
                    newchatroom["createdAt"]= FieldValue.serverTimestamp()

                    //chatsRef.document("4112T5vcMEnnu7ltmX96").collection("messages").add(newchatroom)
                    val chatroomid=chatsRef.document()
                    chatroomid.collection("messages").add(newchatroom)
                    chatsRef.add(newchatroom)

                }
                else
                {
                    //Chatroom ID exist for this sender and receiver
                    chatsRef.document(chatroomid).collection("messages").add(MessageHashMap)
                }


            }


        chatsRef.document("4112T5vcMEnnu7ltmX96").collection("messages").add(MessageHashMap)

 */

    }




}
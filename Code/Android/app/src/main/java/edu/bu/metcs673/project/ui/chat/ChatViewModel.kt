package edu.bu.metcs673.project.ui.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.model.chat.MessageModel

class ChatViewModel(application: Application, val currentChatId: String) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "ChatViewModel"
    }

    private val chatsRef: CollectionReference = Firebase.firestore.collection("chats")
    private var allMessages: MutableLiveData<MutableList<MessageModel>> = MutableLiveData()

    private var messageListener: ListenerRegistration? = null
    private val messagesList = mutableListOf<MessageModel>()

    init {
        fetchMessages()
    }

    private fun fetchMessages() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        messageListener =
            chatsRef.document(currentChatId).collection("messages").orderBy("createdAt")
                .addSnapshotListener { messages, _ ->
                    messagesList.clear()
                    if (messages != null && !messages.isEmpty) {
                        for (message in messages.documents) {
                            val messageObject = MessageModel(message)
                            messageObject.isOwnMessage = currentUserId == messageObject.userId
                            messagesList.add(messageObject)
                        }
                        allMessages.value = messagesList
                    }
                }
    }

    fun getAllMessages(): LiveData<MutableList<MessageModel>>? {
        return allMessages
    }

    //hashmap
    fun insert(userMessage: MessageModel) {
        messagesList.add(userMessage)
        allMessages.value = messagesList
    }

    fun remove(userMessage: MessageModel) {
        messagesList.remove(userMessage)
        allMessages.value = messagesList
    }

    fun unobserve() {
        messageListener?.remove()
    }
}

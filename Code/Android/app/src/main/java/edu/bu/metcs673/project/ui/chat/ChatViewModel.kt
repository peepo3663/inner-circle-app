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

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "ChatViewModel"
    }

    private val chatsRef: CollectionReference = Firebase.firestore.collection("chats")
    private var allMessages: MutableLiveData<List<MessageModel>> = MutableLiveData()
    private var currentChatId: String? = null

    private var snapshotListener: ListenerRegistration? = null
    private val messagesList = mutableListOf<MessageModel>()

    init {
        fetchMessages()
    }

    private fun fetchMessages() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        snapshotListener = chatsRef.whereArrayContains("users", currentUserId).addSnapshotListener { querySnapshot, _ ->
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    currentChatId = document.id
                    document.reference.collection("messages").orderBy("createdAt").addSnapshotListener { messages, _ ->
                        messagesList.clear()
                        if (messages != null && !messages.isEmpty) {
                            for (message in messages.documents) {
                                val messageObject = message.toObject(MessageModel::class.java)
                                messageObject?.isOwnMessage = currentUserId == messageObject?.userId
                                if (messageObject != null) {
                                    messagesList.add(messageObject)
                                }
                            }
                            allMessages.value = messagesList
                        }
                    }
                }
            }
        }
    }

    fun getAllMessages(): LiveData<List<MessageModel>>? {
        return allMessages
    }

    fun insert(userMessage: MessageModel) {
        if (currentChatId == null) return
        val messageInChat = hashMapOf("text" to userMessage.text, "profile_picture" to userMessage.profile_picture, "userId" to userMessage.userId, "createdAt" to FieldValue.serverTimestamp())
        chatsRef.document(currentChatId!!).collection("messages").add(messageInChat)
    }
}

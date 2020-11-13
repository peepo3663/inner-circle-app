package edu.bu.metcs673.project.ui.message

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.model.message.ChatRoomModel

class MessageListViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        private const val TAG = "MessageListViewModel"
    }

    private val chatsRef: CollectionReference = Firebase.firestore.collection("chats")
    private var allMessageList: MutableLiveData<List<ChatRoomModel>> = MutableLiveData()

    private var snapshotListener: ListenerRegistration? = null
    private var chatRooms = mutableListOf<ChatRoomModel>()
    init {
        fetchChatrooms()
    }

    private fun fetchChatrooms() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        snapshotListener = chatsRef.whereArrayContains("userIds", userId).addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.e(TAG, e.message, e)
                return@addSnapshotListener
            }
            chatRooms.clear()
            if (querySnapshot != null && querySnapshot.isEmpty.not()) {
                querySnapshot.documents.forEach { documentSnapshot ->
                    val chatRoomModel = ChatRoomModel(documentSnapshot.id, documentSnapshot.data)
                    chatRooms.add(chatRoomModel)
                }
                allMessageList.value = chatRooms
            }
        }
    }

    fun getAllChatrooms(): MutableLiveData<List<ChatRoomModel>> {
        return allMessageList
    }
}
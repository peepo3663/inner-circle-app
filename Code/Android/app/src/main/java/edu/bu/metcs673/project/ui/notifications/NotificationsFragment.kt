package edu.bu.metcs673.project.ui.notifications


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.chat.SearchAdapter
import edu.bu.metcs673.project.core.ICApp
import edu.bu.metcs673.project.model.message.ChatRoomModel
import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.ui.base.BaseFragment
import edu.bu.metcs673.project.ui.listener.ChatRoomClickListener
import edu.bu.metcs673.project.ui.listener.OnFriendClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * @brief Class to search for all Users registered to Inner Circle Application
 */
class NotificationsFragment : BaseFragment(), OnFriendClickListener {
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var recyclerView: RecyclerView
    private var searchadapter: SearchAdapter? = null
    private val mUsers = mutableListOf<User>()
    private val usersRef: CollectionReference = Firebase.firestore.collection("users")
    private val chatRef: CollectionReference = Firebase.firestore.collection("chats")

    // private var mListener: ListenerRegistration? = null
    private var chatRoomClickListener: ChatRoomClickListener? = null
    private var searchUserEmail: EditText? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            chatRoomClickListener = context as ChatRoomClickListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ChatRoomClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        searchUserEmail = view.findViewById(R.id.searchUserEmail)
        recyclerView = view.findViewById(R.id.friendList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        mUsers.clear()

        //Search for all firebase users
        getAllFirebaseUsers()
        searchUserEmail?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            //On user input, filter out users based off character sequence
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() == true) {
                    getAllFirebaseUsers()
                    return
                }
                searchforUserEmail(s.toString().toLowerCase(Locale.getDefault()))
            }
        })

        return view
    }

    /**
     * Returns recycleView containing user emails that match input sequence.
     */
    private fun searchforUserEmail(str: String) {
        val queryEmails = usersRef.orderBy("email").startAt(str)
            .endAt(str + "\uf8ff")
        queryEmails.get().addOnCompleteListener { querySnapshotTask ->
            val querySnapshot = querySnapshotTask.result
            if (querySnapshot != null) {
                mUsers.clear()

                if (querySnapshot.documents.isEmpty()) {
                    updateDataSource()
                    return@addOnCompleteListener
                }
                //Iterate through all registered users in application
                for (eachUser in querySnapshot.documents) {
                    val currentUser = User(eachUser)
                    if (currentUser.userId == currentUserId) {
                        continue
                    }
                    mUsers.add(currentUser)
                    filterOutWhoHaveChatRoomWith(currentUser)
                }
            }
        }
    }

    /**
     * returns recycleView containing all users.
     */
    private fun getAllFirebaseUsers() {
        val queryUsers = usersRef.orderBy("email")
        showLoader(true)
        queryUsers.get().addOnCompleteListener { querySnapshotTask ->
            showLoader(false)
            val querySnapshot = querySnapshotTask.result
            if (querySnapshot != null) {
                if (searchUserEmail?.text.toString().isEmpty()) {
                    mUsers.clear()
                    //Iterate through all registered users in application
                    if (querySnapshot.documents.isEmpty()) {
                        updateDataSource()
                        return@addOnCompleteListener
                    }
                    for (eachUser in querySnapshot.documents) {
                        val currentUser = User(eachUser)
                        if (currentUser.userId == currentUserId) {
                            continue
                        }
                        mUsers.add(currentUser)
                        filterOutWhoHaveChatRoomWith(currentUser)
                    }
                }
            }
        }
    }

    private fun updateDataSource() {
        if (searchadapter == null) {
            searchadapter = SearchAdapter(mUsers, this)
            recyclerView.adapter = searchadapter
        } else {
            searchadapter?.mUsers = mUsers
            searchadapter?.notifyDataSetChanged()
        }
    }

    override fun onItemClicked(friend: User) {
        val options = arrayOf(
            "Send Message"
        )

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("What do you want to do?")
        builder.setItems(options) { dialog, position ->
            if (position == 0) {
                checkTheChatroom(friend)
            }
            dialog.dismiss()
        }
        builder.show()
    }

    private fun checkTheChatroom(friend: User) {
        val currentUser = (activity?.application as ICApp).currentUser
        if (currentUser != null) {
            val users = ArrayList<Map<String, Any?>>()
            users.apply {
                add(friend.toMap())
                add(currentUser.toMap())
            }
            val chatRoomModel = ChatRoomModel(chatRoomId = null, data = mapOf("users" to users))
            chatApi.createChatRoom(chatRoomModel).enqueue(object : Callback<ChatRoomModel> {
                override fun onFailure(call: Call<ChatRoomModel>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ChatRoomModel>,
                    response: Response<ChatRoomModel>
                ) {
                    if (response.isSuccessful) {
                        chatRoomClickListener?.chatRoomClick(response.body() as ChatRoomModel)
                    } else {
                        createChatRoomError(response)
                    }
                }

            })
        }
    }

    private fun createChatRoomError(response: Response<ChatRoomModel>) {

    }

    private fun filterOutWhoHaveChatRoomWith(friend: User) {
        val currentUserId = currentUserId ?: return
        var query = chatRef.whereArrayContains("userIds", friend.userId)
        val task = query.get()
        task.addOnCompleteListener { completedTask ->
            if (completedTask.isSuccessful && completedTask.result?.isEmpty == false) {
                val documents = completedTask.result?.documents as List<DocumentSnapshot>
                val friendsToRemove = mutableListOf<User>()
                for (document in documents) {
                    val chatRoomModel = ChatRoomModel(document.id, document.data)
                    if (chatRoomModel.userIds?.containsAll(
                            listOf(
                                friend.userId,
                                currentUserId
                            )
                        ) == true
                    ) {
                        friendsToRemove.add(friend)
                    }
                }
                mUsers.removeAll(friendsToRemove)
                updateDataSource()
            }
        }
    }

}
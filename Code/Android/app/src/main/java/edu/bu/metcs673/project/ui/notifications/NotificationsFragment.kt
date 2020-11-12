package edu.bu.metcs673.project.ui.notifications


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.chat.SearchAdapter
import edu.bu.metcs673.project.model.chat.UserEmailModel


//@brief Class to search for all Users registered to Inner Circle Application
class NotificationsFragment : Fragment() {
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var recyclerView: RecyclerView
    private var searchadapter: SearchAdapter?=null
    private val mUsers = mutableListOf<UserEmailModel>()
    private val usersRef: CollectionReference = Firebase.firestore.collection("users")
    private var snapshotListener: ListenerRegistration? = null
    private var searchEmailText: EditText?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        recyclerView = view.findViewById(R.id.recycleviewtest)

        recyclerView.layoutManager=LinearLayoutManager(activity)

        searchEmailText=view.findViewById(R.id.searchuseremail)

        mUsers.clear()

        //Search for all firebase users
        GetAllFirebaseUsers()

        searchEmailText!!.addTextChangedListener(object: TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            //On user input, filter out users based off character sequence
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchforUserEmail(s.toString().toLowerCase())
            }

        })

        return view
    }

    //@return Returns recycleView containing user emails that match input sequence
    private fun searchforUserEmail(str:String)
    {
        var myFirebaseID=FirebaseAuth.getInstance().currentUser!!.uid

        val queryEmails= Firebase.firestore.collection("users").orderBy("email").startAt(str).endAt(str+"\uf8ff")

        queryEmails.addSnapshotListener { querySnapshot, _ ->
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                mUsers.clear()

                //Iterate through all registered users in application
                for (eachuser in querySnapshot.documents) {
                    val eachuserObject = eachuser.toObject(UserEmailModel::class.java)
                    if (eachuserObject != null) {
                        mUsers.add(eachuserObject)
                    }
                }
                searchadapter=SearchAdapter(context!!,mUsers)
                recyclerView.adapter =searchadapter
            }

        }
    }

    //@return Returns recycleView containing all users
    private fun GetAllFirebaseUsers(){

        var myFirebaseID=FirebaseAuth.getInstance().currentUser!!.uid

        //var allUsersFirebaseEmails=FirebaseDatabase.getInstance().reference.child("users")

        snapshotListener = usersRef.addSnapshotListener { querySnapshot, _ ->
            if (querySnapshot != null && !querySnapshot.isEmpty)
            {
                if (searchEmailText!!.text.toString()=="")
                {

                    mUsers.clear()

                    //Iterate through all registered users in application
                    for (eachuser in querySnapshot.documents) {
                        val eachuserObject = eachuser.toObject(UserEmailModel::class.java)
                        if (eachuserObject != null) {
                            mUsers.add(eachuserObject)
                        }
                    }
                    searchadapter=SearchAdapter(context!!,mUsers!!)
                    recyclerView.adapter =searchadapter
                }
            }
        }
    }

}
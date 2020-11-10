package edu.bu.metcs673.project.ui.notifications

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.LogInActivity
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.adapter.chat.SearchAdapter
import edu.bu.metcs673.project.model.chat.MessageModel
import edu.bu.metcs673.project.model.chat.UserEmailModel
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment() {

    private var searchadapter: SearchAdapter?=null
    private val mUsers = mutableListOf<UserEmailModel>()
    private val usersRef: CollectionReference = Firebase.firestore.collection("users")
    private var snapshotListener: ListenerRegistration? = null
    private var recyclerView:RecyclerView?=null
    private var searchEmailText: EditText?=null


    /*
    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })
        return root
    }

     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        recyclerView= view?.findViewById(R.id.searchuseremaiList)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager=LinearLayoutManager(context)
        searchEmailText=view?.findViewById(R.id.searchuseremail)

        mUsers.clear()
        GetAllFirebaseUsers()

        searchEmailText!!.addTextChangedListener(object: TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchforUserEmail(s.toString().toLowerCase())
            }

        })

        return root

        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun GetAllFirebaseUsers(){

        var myFirebaseID=FirebaseAuth.getInstance().currentUser!!.uid

        //var allUsersFirebaseEmails=FirebaseDatabase.getInstance().reference.child("users")


        snapshotListener = usersRef.addSnapshotListener { querySnapshot, _ ->
            if (querySnapshot != null && !querySnapshot.isEmpty)
            {
                if (searchEmailText!!.text.toString()=="")
                {
                    //Iterate through all registered users in application
                    for (eachuser in querySnapshot.documents) {
                        val eachuserObject = eachuser.toObject(UserEmailModel::class.java)
                        if (eachuserObject != null) {
                            mUsers.add(eachuserObject)
                        }
                    }
                    searchadapter=SearchAdapter(context!!,mUsers!!)
                    recyclerView!!.adapter =searchadapter
                }
            }
        }
    }

    private fun searchforUserEmail(str:String)
    {
        var myFirebaseID=FirebaseAuth.getInstance().currentUser!!.uid

        val queryEmails= Firebase.firestore.collection("users").orderBy("email").startAt(str).endAt(str+"uf8ff")

        queryEmails.addSnapshotListener { querySnapshot, _ ->
            if (querySnapshot != null && !querySnapshot.isEmpty) {
                //Iterate through all registered users in application
                for (eachuser in querySnapshot.documents) {
                    val eachuserObject = eachuser.toObject(UserEmailModel::class.java)
                    if (eachuserObject != null) {
                        mUsers.add(eachuserObject)
                    }
                }
                searchadapter=SearchAdapter(context!!,mUsers!!)
                recyclerView!!.adapter =searchadapter
            }

        }




    }



}
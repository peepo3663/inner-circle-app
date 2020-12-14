package edu.bu.metcs673.project.ui.profile

import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.ui.base.BaseActivity
import edu.bu.metcs673.project.util.BundleExtraKeys
import kotlinx.android.synthetic.main.activity_view_profile.*

class ViewProfileActivity : BaseActivity() {

    private val usersRef = Firebase.firestore.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        val userId = intent.getStringExtra(BundleExtraKeys.USER_ID)
        if (userId == null) {
            Toast.makeText(this, "User id is invalid.", Toast.LENGTH_LONG).show()
            onBackPressed()
        }
        loadUserDetail(userId as String)
    }

    private fun loadUserDetail(userId: String) {
        usersRef.document(userId).get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                val profileResult = it.result as DocumentSnapshot
                Glide.with(this).load(profileResult["profile_picture"])
                    .placeholder(R.drawable.ic_launcher_foreground).circleCrop().into(img_profile)
                tv_profile.text = profileResult.get("email") as String?
                title = profileResult.get("name") as String?
            }
        }
    }
}
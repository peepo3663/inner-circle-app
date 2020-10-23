package edu.bu.metcs673.project.model.user

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.net.MalformedURLException
import java.net.URL

class User(documentSnapshot: DocumentSnapshot) {
    private val documentSnapshotData: Map<String, Any>
    val email: String
    val name: String
    var profilePicture: URL? = null
    var createdAt: Timestamp? = null
    var updatedAt: Timestamp? = null

    init {
        if (documentSnapshot.data == null) {
            throw NullPointerException()
        }
        documentSnapshotData = documentSnapshot.data as Map<String, Any>
        email = documentSnapshotData["email"] as String
        name = documentSnapshotData["name"] as String
        createdAt = documentSnapshotData["createdAt"] as Timestamp
        updatedAt = documentSnapshotData["updatedAt"] as Timestamp
        val profilePictureString = documentSnapshot["profile_picture"] as String?
        profilePictureString?.let {
            profilePicture = try { URL(it) } catch (e: MalformedURLException) { null }
        }
    }
}
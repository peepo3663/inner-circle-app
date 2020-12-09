package edu.bu.metcs673.project.model.user

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.net.MalformedURLException
import java.net.URL

class User(userId: String, map: Map<String, Any?>?) {
    var userId: String
    var email: String
    var name: String
    var profilePicture: URL? = null
    var createdAt: Timestamp? = null
    var updatedAt: Timestamp? = null

    init {
        this.userId = userId
        this.email = map?.get("email") as String
        this.name = map["name"] as String
        this.createdAt = map.get("createdAt") as Timestamp?
        this.updatedAt = map.get("updatedAt") as Timestamp?
        val profilePictureString = map["profile_picture"] as String?
        profilePictureString?.let {
            profilePicture = try { URL(it) } catch (e: MalformedURLException) { null }
        }
    }

    constructor(documentSnapshot: DocumentSnapshot): this(documentSnapshot.id, documentSnapshot.data)
}
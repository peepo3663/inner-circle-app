package edu.bu.metcs673.project.model.user

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.annotations.SerializedName
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

class User(userId: String, map: Map<String, Any?>?) {

    @SerializedName("uid")
    var userId: String

    var email: String
    var name: String

    @SerializedName("profile_picture")
    var profilePicture: URL? = null

    @SerializedName("createdAt")
    var createdAt: Date? = null

    @SerializedName("updatedAt")
    var updatedAt: Date? = null

    init {
        this.userId = userId
        this.email = map?.get("email") as String
        this.name = map["name"] as String
        val createdAt = map.get("createdAt") as Timestamp?
        val updatedAt = map.get("updatedAt") as Timestamp?
        if (createdAt != null) {
            this.createdAt = createdAt.toDate()
        }
        if (updatedAt != null) {
            this.updatedAt = updatedAt.toDate()
        }
        val profilePictureString = map["profile_picture"] as String?
        profilePictureString?.let {
            profilePicture = try { URL(it) } catch (e: MalformedURLException) { null }
        }
    }

    constructor(documentSnapshot: DocumentSnapshot): this(documentSnapshot.id, documentSnapshot.data)

    fun toMap(): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>(
            "name" to this.name,
            "uid" to this.userId,
            "email" to this.email,
            "profile_picture" to this.profilePicture?.toString()
        )
        return map
    }
}
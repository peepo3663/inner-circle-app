package edu.bu.metcs673.project.model.user

import com.google.firebase.firestore.DocumentSnapshot

class UserDevice(var userDeviceId: String?, map: Map<String, Any?>?) {

    var deviceToken: String = map?.get("deviceToken") as String
    var model: String? = null
    var osVersion: String? = null

    constructor(documentSnapshot: DocumentSnapshot): this(documentSnapshot.id, documentSnapshot.data)
    constructor(deviceToken: String): this(null, mapOf("deviceToken" to deviceToken))

    init {
        if (userDeviceId == null) {
            userDeviceId = map?.get("userDeviceId") as String?
        }
        model = map?.get("model") as String?
        osVersion = map?.get("osVersion") as String?
    }
}
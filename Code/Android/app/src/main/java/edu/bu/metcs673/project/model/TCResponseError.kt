package edu.bu.metcs673.project.model

import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject


class TCResponseError(errorBody: ResponseBody?) {
    var errorMsg: String? = ""
        private set

    var httpStatus: Int = 0
        private set

    init {
        run {
            if (errorBody == null) {
                return@run
            }
            val jsonObject = try { JSONObject(errorBody.string()) } catch (e: JSONException) { null }
                ?: return@run
            errorMsg = jsonObject.getString("errorMsg")
            httpStatus = jsonObject.getInt("status")
        }
    }
}
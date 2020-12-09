package edu.bu.metcs673.project.api

import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.model.user.UserDevice
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @POST("/users/create")
    fun loginUser(@Body userData: User): Call<User>

    @POST("/users/update/{userId}/deviceToken")
    fun updateTheDeviceToken(@Path("userId") userId: String, @Body userDevice: UserDevice): Call<Map<String, String>>
}
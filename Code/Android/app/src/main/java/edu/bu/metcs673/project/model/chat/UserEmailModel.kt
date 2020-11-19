package edu.bu.metcs673.project.model.chat

//@brief class definition to store user email and name from retrieved firebase objects
//@note Simpler to manage data this manner
data class UserEmailModel (
    var email: String? = null,
    var name: String? = null
)
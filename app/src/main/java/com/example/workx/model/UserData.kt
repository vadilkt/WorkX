package com.example.workx.model
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserData (
    var firstName: String? = "",
    var lastName: String? = "",
    var email: String? = "",
    var phoneNumber: String? = ""
)

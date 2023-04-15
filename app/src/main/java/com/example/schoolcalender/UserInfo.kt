package com.example.schoolcalender

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("school") val school: String?,
    @SerializedName("j_username") val userName: String?,
    @SerializedName("j_password") val password: String?,
    @SerializedName("token") val token: String?,
    )

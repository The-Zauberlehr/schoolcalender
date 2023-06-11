package com.example.schoolcalender

import android.provider.ContactsContract.Data
import com.google.gson.annotations.SerializedName

data class jsondata( //Daten die das Json Array enthälta

    @SerializedName("periodText")
    var text: String?,
    @SerializedName("lessonCode")
    var lessonCode:String?,
    @SerializedName("date")
    var date: Int?,
    @SerializedName("startTime")
    var startTime:Int?,
    @SerializedName("endTime")
    var endTime:Int?,
    @SerializedName("studentGroup")
    var studentGroup:String?,
    @SerializedName("cellState")
    var cellState:String?
)

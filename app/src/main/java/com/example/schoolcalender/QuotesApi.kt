package com.example.schoolcalender
import okhttp3.Cookie
import retrofit2.Response

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface QuotesApi {
    @POST("/WebUntis/j_spring_security_check")

    fun login(@Body userData: UserInfo): Call<UserInfo>

    @Headers("Content-Type: application/json")
    @GET("/WebUntis/api/public/timetable/weekly/data?elementType=5&elementId=122&date=2023-04-24")
    suspend fun getQuotes() : Response<QuoteList>
}
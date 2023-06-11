package com.example.schoolcalender
import android.app.Application
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import okhttp3.RequestBody
import retrofit2.Response


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface QuotesApi{
    @GET("/WebUntis/?school=BSZ+Wirtschaft+Dresden#/basic/login")
     fun getcookies(): Call<ResponseBody>
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
     @POST("/WebUntis/j_spring_security_check")
    fun login(@Field("school")school:String,@Field ("j_username")username:String,@Field ("j_password") password:String,@Field("token") token:String): Call<ResponseBody>
    @Headers("Content-Type: application/json")
    @GET
    suspend fun getQuotes(@Url strdate:String ) : Response<JsonObject>
    @Headers("Content-Type: application/json")
    @GET
    suspend fun authorizationgetter(@Url strdate:String): Response<JsonPrimitive>
    @Headers("Content-Type: application/json")
    @GET
    suspend fun id(@Url strdate:String ) : Response<JsonObject>
}
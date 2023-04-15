package com.example.schoolcalender

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Context
import android.content.Intent
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONStringer

import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fun gettimetabel() {

            val quotesApi = RetrofitHelper.getInstance().create(QuotesApi::class.java)
            // launching a new coroutine
            GlobalScope.launch {
                val result = quotesApi.getQuotes()
                if (result != null)
                // Checking the results
                    Log.d("ayush: ", result.body().toString())
                println(result.body().toString())
            }
        }
        fun postlogin(){
            val apiService = RestapiService()
            val userInfo = UserInfo(  school = "bsz wirtschaft dresden",
                userName = "Seiadr",
                password = "",
                token = "",
                )


            apiService.loginexecute(userInfo) {
                if (it != null) {
                    println("jetzt kommt das wichtige")
                    println(it)
                    // it = newly added user parsed as response
                    // it?.id = newly added user ID
                //} else {
                 //   println("Error registering new user")
                }
            }
        }

        gettimetabel()
    }
}

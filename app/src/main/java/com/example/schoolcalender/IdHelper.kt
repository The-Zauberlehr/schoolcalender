package com.example.schoolcalender

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IdHelper {

    val baseUrl = "https://ajax.webuntis.com"
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val headerInterceptor= object: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("Authorization", authorization)
                .addHeader("cookie", """${schoolid}${traceid};${sessionid}""")

                .build()
            val response = chain.proceed(request)
            return response
        }

    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(logger)

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            // add converter factory to
            // convert JSON object to Java object
            .client(okHttp.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}
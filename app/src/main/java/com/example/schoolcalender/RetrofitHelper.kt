package com.example.schoolcalender
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.Authenticator

import okhttp3.Headers
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory




object RetrofitHelper {
    val baseUrl = "https://ajax.webuntis.com"
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val headerInterceptor= object: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = request.newBuilder()
                .addHeader("cookie", """schoolname="_YnN6IHdpcnRzY2hhZnQgZHJlc2Rlbg=="; JSESSIONID=3E999E1AD54A22F7AAC1EC2228B90014; traceId=6fd65cfbeec6813d498b4a50ad47c8136f3c8bbc""")

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
package com.example.schoolcalender
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.addHeaderLenient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Loginhelper {
    val baseUrl = "https://ajax.webuntis.com"
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val headerInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = request.newBuilder()
                .addHeader(
                    "cookie",
                    """${schoolid}${sessionid}"""
                )

                .build()
            val response = chain.proceed(request)
            return response
        }
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
        )
        .addInterceptor(headerInterceptor)
        .build()

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)


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



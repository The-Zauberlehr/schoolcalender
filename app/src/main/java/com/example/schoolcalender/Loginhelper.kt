package com.example.schoolcalender
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.addHeaderLenient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    val headerInterceptor= object: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = request.newBuilder()
                .addHeader(
                    "cookie",
                    """schoolname="_YnN6IHdpcnRzY2hhZnQgZHJlc2Rlbg=="; JSESSIONID=CFCA0F626C47CB7C705F7E4B1F6BA905; traceId=38301f800eaf500489759cc09ec4cecb3a903356"""
                )
                .addHeader(
                    "X-CSRF-TOKEN",
                    "a0661583-b41f-4992-b2c0-aa8b3a1bdb5e"
                )

                .build()
            val response = chain.proceed(request)
            return response
        }
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(headerInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ajax.webuntis.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}



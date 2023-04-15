package com.example.schoolcalender
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestapiService {
    fun loginexecute(userData: UserInfo, onResult: (UserInfo?) -> Unit) {

        val retrofit = ServiceBuilder.buildService(QuotesApi::class.java)
        retrofit.login(userData).enqueue(
            object : Callback<UserInfo> {
                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    // get headers
                    val headers = response.headers()
                    // get header value
                    val cookie = response.headers().get("Set-Cookie")
                    val CookieString:String = cookie.toString()


                }
            }
        )

    }

}

package com.example.park_for_kids.network

import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        Log.d("HTTP", "Request URL: $url")

        val response = chain.proceed(request)
        Log.d("HTTP", "Response Code: ${response.code}")
        Log.d("HTTP", "Response Body: ${response.peekBody(Long.MAX_VALUE).string()}")
        return response
    }
}
package com.appcake.modsforvalheim.utlis

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthApiKeyInterceptor(context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("API_KEY", "9185042346")
        return chain.proceed(requestBuilder.build())
    }
}
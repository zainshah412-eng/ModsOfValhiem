package com.appcake.modsforvalheim.utlis

import android.content.Context
import android.net.ConnectivityManager
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import android.net.NetworkCapabilities
import android.os.Build
import com.appcake.modsforvalheim.R
import okhttp3.Interceptor
import okhttp3.Response
import toast

class NetworkInterceptor(context: Context) : Interceptor {

    private val mContext: Context = context
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            LoggerUtils.info("Network", "no internet")
            runOnUiThread(Runnable {
                mContext.toast(mContext.resources.getString(R.string.no_internet))
            })
            throw NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }
        val requestBuilder = chain.request().newBuilder()
        return chain.proceed(requestBuilder.build())
    }
    private fun isConnected(): Boolean {
        var result = false
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}
package com.appcake.modsforvalheim.data.remote

import com.appcake.modsforvalheim.utlis.ErrorUtils
import com.appcake.modsforvalheim.utlis.LoggerUtils
import com.appcake.modsforvalheim.utlis.Resource
import com.appcake.modsforvalheim.utlis.apierror.ApiError
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            val message = StringBuilder()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }

            val errorRes = ErrorUtils.parseError(response?.errorBody()?.string())
            LoggerUtils.debug("Error", errorRes.toString())
            return error(errorRes!!)
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: ApiError): Resource<T> {
        return Resource.error(message)
    }

}
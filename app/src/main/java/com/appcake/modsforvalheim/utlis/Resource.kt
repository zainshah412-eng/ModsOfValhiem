package com.appcake.modsforvalheim.utlis

import com.appcake.modsforvalheim.utlis.apierror.ApiError

data class Resource<out T>(val status: Status, val data: T?, val message: ApiError?) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: ApiError, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}
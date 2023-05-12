package com.appcake.modsforvalheim.data.remote

import com.appcake.modsforvalheim.utlis.AppConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(AppConstants.GET_SEARCH_DETAIL)
    suspend fun getSpecificMods(
        @Query("q") searchReference: String,
    ): Response<String>

}
package com.appcake.modsforvalheim.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) : BaseDataSource() {

    suspend fun getSpecificMods(searchReference: String) =
        getResult {
            apiService.getSpecificMods(searchReference)
        }
}
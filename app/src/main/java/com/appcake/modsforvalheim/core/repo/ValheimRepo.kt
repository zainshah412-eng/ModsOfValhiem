package com.appcake.modsforvalheim.core.repo

import com.appcake.modsforvalheim.data.remote.RemoteDataSource
import com.appcake.modsforvalheim.data.remote.performGetOperation
import javax.inject.Inject

class ValheimRepo@Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    fun getSpecificMods(searchReference: String) =
        performGetOperation(
            networkCall = { remoteDataSource.getSpecificMods(searchReference) }
        )
}
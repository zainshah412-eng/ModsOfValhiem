package com.appcake.modsforvalheim.core.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.appcake.modsforvalheim.core.repo.ValheimRepo
import com.appcake.modsforvalheim.utlis.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ValheimViewModel @Inject constructor(
    private val androidTestAssignmentRepo: ValheimRepo,
) : ViewModel() {

    private val _searchMods = MutableLiveData<String>()

    private val searchMods = _searchMods.switchMap { token ->
        androidTestAssignmentRepo.getSpecificMods(token)
    }

    //LiveData
    val androidTestAssignmentResp: LiveData<Resource<String>> = searchMods
    //Functions
    fun setSearchReference(
        searchRef: String
    ) {
        _searchMods.value = searchRef
    }
}
package com.appcake.modsforvalheim.core.model

data class DropDownData (var dropdownTitle:String ,var dropdownDescription:String){
    fun setFaqTitle1(ddTitle:String){
        dropdownTitle = ddTitle
    }

    fun setFaqDescription1(ddDesc:String){
        dropdownDescription = ddDesc
    }
}
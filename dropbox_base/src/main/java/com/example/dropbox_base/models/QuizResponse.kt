package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class QuizResponse(

    @field:SerializedName("_f1j1b0_lz")
    val f1j1b0Lz: String? = null,

    @field:SerializedName("_f1j1b0_e7q")
    val f1j1b0E7q: String? = null,

    @field:SerializedName("_f1j1b0_list")
    val f1j1b0List: F1j1b0List? = null
)


data class QuizItem(

    @field:SerializedName("_f1j1b0t3-right")
    val f1j1b0t3Right: String? = null,

    @field:SerializedName("new")
    val jsonMemberNew: String? = null,

    @field:SerializedName("_f1j1b0i1-answ")
    val f1j1b0i1Answ: List<String?>? = null,

    @field:SerializedName("_f1j1b0d4-quest")
    val f1j1b0d4Quest: String? = null
)

data class F1j1b0List(

    @field:SerializedName("Quiz")
    val quiz: List<QuizItem>? = null,

    @field:SerializedName("_f1j1b0_l-y")
    val f1j1b0LY: String? = null,

    @field:SerializedName("Favorites")
    val favorites: List<Any?>? = null
)
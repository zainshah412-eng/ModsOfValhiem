package com.example.dropbox_base.models
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class NamesGeneratorResponse(
    @SerializedName("e7jolb-_79mq")
    @Expose val e7jolb79mq: String,
    @SerializedName("e7jolb-_list")
    @Expose val e7jolbList: E7jolbList,
    @SerializedName("e7jolb-_tjqi")
    @Expose val e7jolbTjqi: String
)

data class E7jolbList(
    @SerializedName("e7jolb-_9hh")
    @Expose val e7jolb9hh: String,
    @SerializedName("e7jolb-_v-")
    @Expose val e7jolbV: String,
    @SerializedName("e7jolb-_w")
    @Expose val e7jolbW: String,
    @SerializedName("Favorites")
    @Expose val favorites: List<Any>,
    @SerializedName("Male names")
    @Expose val maleNames: List<MaleName>,
    @SerializedName("Women's names")
    @Expose val womensNames: List<WomensName>
)

data class MaleName(
    @SerializedName("e7jolb-d4")
    @Expose val e7jolbD4: String,
    @SerializedName("new")
    @Expose val new: String
)

data class WomensName(
    @SerializedName("e7jolb-d4")
    @Expose val e7jolbD4: String,
    @SerializedName("new")
    @Expose val new: String
)
package com.appcake.modsforvalheim.core.model

class DummyClassData(
    var titleMods: String,
    var descMods: String,
    var imageListMods: List<String>,
    var imageUrl: String? = null,
    var isRequestedForImageUrl: Boolean? = false
) {
    private fun setNameMods() {
        val nmMods = titleMods
    }

    override fun toString(): String {
        return titleMods
    }
}
package com.appcake.modsforvalheim.core.interfaces

interface OnCategoryItemListener {
    fun onCategoryItemClicked(positionSeed: Int, idSeed: Int)
    private fun onCategoryItemClickedSample(positionSeed: Int) {
        val xSeed = positionSeed
    }
}
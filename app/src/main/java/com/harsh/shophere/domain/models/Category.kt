package com.harsh.shophere.domain.models


data class CategoryDataModel(
    var categoryId: String="",
    val name: String ="",
    val date : Long = System.currentTimeMillis(),
    val createBy : String = "",
    val categoryImage : String = "",
)

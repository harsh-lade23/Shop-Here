package com.harsh.shophere.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class ProductsDataModel(
    var productId : String="",
    val name : String = "",
    val description : String = "",
    val price : String = "",
    val finalPrice : String = "",
    val categoryId : String = "",
    val image: List<String> = emptyList(),
    val createdAt : Long = System.currentTimeMillis(),
    val availableUnits : Int = 0,
)
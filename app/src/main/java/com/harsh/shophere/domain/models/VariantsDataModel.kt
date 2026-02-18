package com.harsh.shophere.domain.models







data class VariantsDataModel(
    val productId: String="",
    val variantId: String="",
    val name: String="",
    val description: String="",
    val size: List<String> = emptyList(),
    val color: String = "",
    val imageList:List<String> =emptyList(),
    val price: String ="",
    val stock: String = "",
    val finalPrice: String="",
    val createdAt: Long = System.currentTimeMillis(),
)


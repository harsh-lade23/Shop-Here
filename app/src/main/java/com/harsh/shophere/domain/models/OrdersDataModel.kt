package com.harsh.shophere.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class OrdersData(
    val orderId: String="",
    val userId: String="",
    val totalPrice: String="0",
    val status: String="Pending",
    val shippingDetails: ShippingDetails= ShippingDetails(),
    val shippingMethods: String ="",
    val createdAt: Long= System.currentTimeMillis(),
    val orderItems: List<OrderItem> = emptyList()
): Parcelable



@Serializable
@Parcelize
data class OrderItem(
    val image: String="",
    val name:String="",
    val productId: String="",
    val quantity: Int=0,
    val price: String="0",
    val variantId: String="",
    val selectedOptions: Map<String, String> = emptyMap()
) : Parcelable
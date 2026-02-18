package com.harsh.shophere.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class Address(
    var addressId: String="",
    var country: String="",
    var state: String="",
    var city: String="",
    var village: String="",
    var pinCode: String="",
    var street: String=""
): Parcelable


@Serializable
@Parcelize
data class ShippingDetails(
    var shippingDetailsId: String="",
    var address: Address= Address(),
    var contactNo: String="",
    var email: String="",
    var name:String=""
): Parcelable


package com.harsh.shophere.domain.models



import androidx.compose.runtime.mutableStateMapOf


data class UserData(
    val userId: String="",
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val createdAt: Long= System.currentTimeMillis(),
    val phoneNumber : String = "",
    val address : String = "",
    val profileImage : String = "",
    val isAdmin: Boolean=false,
    var shippingDetails: List<ShippingDetails> = listOf(),
){
    fun toMap() : Map<String, Any>{
        val map = mutableStateMapOf<String, Any>()
        map["userId"]=userId
        map["firstName"] = firstName
        map["lastName"] = lastName
        map["email"] = email
        map["createdAt"] = createdAt
        map["phoneNumber"] = phoneNumber
        map["address"] = address
        map["profileImage"] = profileImage
        map["isAdmin"] = isAdmin
        map["shippingDetails"]=shippingDetails
        return map
    }
}





data class UserDataParent(
    val nodeId : String ="",
    val userData: UserData = UserData()
)




package com.harsh.shophere.presentation.navigation

import kotlinx.serialization.Serializable


sealed class SubNavigation {

    @Serializable
    object LoginSignUpScreen: SubNavigation()

    @Serializable
    object MainHomeScreen: SubNavigation()
}


sealed class Routes{
    @Serializable
    object LoginScreen

    @Serializable
    object SingUpScreen

    @Serializable
    object HomeScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object WishListScreen

    @Serializable
    object CartScreen

    @Serializable
    object CheckOutScreen

    @Serializable
    object PayScreen

    @Serializable
    object ShippingInformationScreen

    @Serializable
    object SeeAllProducts

    @Serializable
    data class EachProductDetailsScreen(val productId: String)

    @Serializable
    object AllCategoriesScreen

    @Serializable
    object AccountScreen

    @Serializable
    object OrderConfirmationScreen


    @Serializable
    data class EachCategoryItemsScreen(val categoryId : String, var categoryName: String)

}
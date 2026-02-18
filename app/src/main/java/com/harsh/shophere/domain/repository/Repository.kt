package com.harsh.shophere.domain.repository

import android.net.Uri
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.BannerDataModel
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.models.CategoryDataModel
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.models.UserDataParent
import com.harsh.shophere.domain.models.VariantsDataModel
import com.harsh.shophere.domain.models.WishListItemModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun registerUserWithEmailAndPassword(userData: UserData,  password: String): Flow<ResultState<String>>
    fun loginUserWithEmailAndPassWord(userData: UserData, password: String): Flow<ResultState<String>>
    fun getUserById(uid: String): Flow<ResultState<UserDataParent>>
    fun updateUserData(userData: UserData): Flow<ResultState<String>>
    fun getCategoriesInLimit(): Flow<ResultState<List<CategoryDataModel>>>
    fun getProductsInLimit(): Flow<ResultState<List<ProductsDataModel>>>
    fun getAllProducts(): Flow<ResultState<List<ProductsDataModel>>>
    fun getProductById(productId: String): Flow<ResultState<ProductsDataModel>>
    fun addToCart(cartDataModel: CartItemModel): Flow<ResultState<String>>
    fun addToWishlist(wishListItemModel: WishListItemModel): Flow<ResultState<String>>
    fun getWishlist(): Flow<ResultState<List<WishListItemModel>>>
    fun getCart(): Flow<ResultState<List<CartItemModel>>>
    fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>>
    fun getCheckout(productId: String): Flow<ResultState<ProductsDataModel>>
    fun getBanners(): Flow<ResultState<List<BannerDataModel>>>
    fun getSpecificCategoryItems(categoryId:String): Flow<ResultState<List<ProductsDataModel>>>
    fun getAllSuggestedProducts(): Flow<ResultState<List<ProductsDataModel>>>
    fun addToOrder(ordersData: OrdersData): Flow<ResultState<String>>
//    fun getMultipleProductsById(productIdList:List<String>)

    fun getOrderList(): Flow<ResultState<List<OrdersData>>>
    fun addOneQuantityIntoTheCartItem(cartItemModel: CartItemModel): Flow<ResultState<String>>
    fun reduceOneQuantityFromCartItem(cartItemModel: CartItemModel): Flow<ResultState<String>>
    fun searchProductInMainScreen(name:String): Flow<ResultState<List<ProductsDataModel>>>
    fun searchWishListByName(name:String): Flow<ResultState<List<WishListItemModel>>>
    fun searchCategoriesByName(name:String): Flow<ResultState<List<CategoryDataModel>>>
    fun clearAllCartItems(): Flow<ResultState<String>>
    fun removeItemFromWishlist(wishListItemModel: WishListItemModel): Flow<ResultState<String>>


    fun addShippingDetails(shippingDetails: ShippingDetails):Flow<ResultState<String>>
    fun getShippingDetailList():Flow<ResultState<List<ShippingDetails>>>
    fun getVariantList(productId: String): Flow<ResultState<List<VariantsDataModel>>>
}
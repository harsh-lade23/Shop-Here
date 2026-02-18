package com.harsh.shophere.presentation.viewModels.states

import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.models.CategoryDataModel
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.models.UserDataParent
import com.harsh.shophere.domain.models.VariantsDataModel
import com.harsh.shophere.domain.models.WishListItemModel


data class ProfileScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: UserDataParent? = null
)

    data class  SearchProductByNameState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val userData: List<ProductsDataModel?> = emptyList(),
    )



data class SearchCategoryByNameState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<CategoryDataModel>? = null,
)

data class AddShippingDetailsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null,)


data class SearchWishlistByNameState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null,
)



data class GetOrderListState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<OrdersData>? = null,
)

data class IncrementCartQuantityState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null,
)

data class DecrementCartQuantityState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null,
)

data class ClearCartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null,
)


data class RemoveWishListItemState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null,
)


data class AddToOrdersState(
    val isLoading: Boolean=false,
    val errorMessage: String?=null,
    val userData:String?=null
)

data class SignUpScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)


data class LoginScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class GetShippingDetailListState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<ShippingDetails>? = null
)

data class UpdateScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class UploadUserProfileImageScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class AddToCartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)


data class GetProductByIdState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val variantErrorMessage: String?=null,
    val productData: ProductsDataModel? = null,
    val variantDataList: List<VariantsDataModel>?=null
)


data class AddToWishListState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)


data class GetAllWishlistState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<WishListItemModel> = emptyList()
)


data class GetAllProductState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val userData: List<ProductsDataModel?> = emptyList()
)


data class GetCartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<CartItemModel?> = emptyList()
)

data class GetAllCategoriesState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<CategoryDataModel?> = emptyList()
)

data class GetCheckoutState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: ProductsDataModel? = null
)

data class GetSpecificCategoryItemsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<ProductsDataModel?> = emptyList()
)

data class GetAllSuggestedProductState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<ProductsDataModel?> = emptyList()
)
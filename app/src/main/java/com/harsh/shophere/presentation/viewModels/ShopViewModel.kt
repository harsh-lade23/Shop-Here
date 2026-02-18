package com.harsh.shophere.presentation.viewModels


import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import com.harsh.shophere.common.HomeScreenState_2
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.usecase.AddShippingDetailsUseCase
import com.harsh.shophere.domain.usecase.AddToCartUseCase
import com.harsh.shophere.domain.usecase.AddToOrderUseCase
import com.harsh.shophere.domain.usecase.AddToWishlistUseCase
import com.harsh.shophere.domain.usecase.ClearCartUseCase
import com.harsh.shophere.domain.usecase.CreateUserUseCase
import com.harsh.shophere.domain.usecase.GetAllCategoryUseCase
import com.harsh.shophere.domain.usecase.GetAllProductUseCase
import com.harsh.shophere.domain.usecase.GetAllSuggestedProductListUseCase
import com.harsh.shophere.domain.usecase.GetBannerListUseCase
import com.harsh.shophere.domain.usecase.GetCartUseCase
import com.harsh.shophere.domain.usecase.GetCategoryInLimitUseCase
import com.harsh.shophere.domain.usecase.GetCheckoutUseCase
import com.harsh.shophere.domain.usecase.GetOrderListUseCase
import com.harsh.shophere.domain.usecase.GetProductByIdUseCase
import com.harsh.shophere.domain.usecase.GetProductsInLimitUseCase
import com.harsh.shophere.domain.usecase.GetShippingDetailListUseCase
import com.harsh.shophere.domain.usecase.GetSpecificCategoryItemsUseCase
import com.harsh.shophere.domain.usecase.GetUserUseCase
import com.harsh.shophere.domain.usecase.GetVariantListUseCase
import com.harsh.shophere.domain.usecase.GetWishlistUseCase
import com.harsh.shophere.domain.usecase.IncrementCartQuantityUseCase
import com.harsh.shophere.domain.usecase.LoginUserUseCase
import com.harsh.shophere.domain.usecase.ReduceOneCartQuantityUseCase
import com.harsh.shophere.domain.usecase.RemoveItemFromWishListUseCase
import com.harsh.shophere.domain.usecase.SearchProductByNameUseCase
import com.harsh.shophere.domain.usecase.UpdateUserDataUseCase
import com.harsh.shophere.presentation.viewModels.states.AddShippingDetailsState
import com.harsh.shophere.presentation.viewModels.states.AddToCartState
import com.harsh.shophere.presentation.viewModels.states.AddToOrdersState
import com.harsh.shophere.presentation.viewModels.states.AddToWishListState
import com.harsh.shophere.presentation.viewModels.states.ClearCartState
import com.harsh.shophere.presentation.viewModels.states.DecrementCartQuantityState
import com.harsh.shophere.presentation.viewModels.states.GetAllCategoriesState
import com.harsh.shophere.presentation.viewModels.states.GetAllProductState
import com.harsh.shophere.presentation.viewModels.states.GetAllSuggestedProductState
import com.harsh.shophere.presentation.viewModels.states.GetAllWishlistState
import com.harsh.shophere.presentation.viewModels.states.GetCartState
import com.harsh.shophere.presentation.viewModels.states.GetCheckoutState
import com.harsh.shophere.presentation.viewModels.states.GetOrderListState
import com.harsh.shophere.presentation.viewModels.states.GetProductByIdState
import com.harsh.shophere.presentation.viewModels.states.GetShippingDetailListState
import com.harsh.shophere.presentation.viewModels.states.GetSpecificCategoryItemsState
import com.harsh.shophere.presentation.viewModels.states.IncrementCartQuantityState
import com.harsh.shophere.presentation.viewModels.states.LoginScreenState
import com.harsh.shophere.presentation.viewModels.states.ProfileScreenState
import com.harsh.shophere.presentation.viewModels.states.RemoveWishListItemState
import com.harsh.shophere.presentation.viewModels.states.SearchProductByNameState
import com.harsh.shophere.presentation.viewModels.states.SignUpScreenState
import com.harsh.shophere.presentation.viewModels.states.UpdateScreenState
import com.harsh.shophere.presentation.viewModels.states.UploadUserProfileImageScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShopViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val upDateUserDataUseCase: UpdateUserDataUseCase,
    private val getCheckoutUseCase: GetCheckoutUseCase,

// Category and Product-related Use Cases
    private val getCategoryInLimitUseCase: GetCategoryInLimitUseCase,
    private val getProductsInLimitUseCase: GetProductsInLimitUseCase,
    private val getProductByIDUseCase: GetProductByIdUseCase,
    private val getAllCategoriesUseCase: GetAllCategoryUseCase,
    private val getSpecificCategoryItemsUseCase: GetSpecificCategoryItemsUseCase,
    private val getAllSuggestedProductsUseCase: GetAllSuggestedProductListUseCase,
    private val getAllProductsUseCase: GetAllProductUseCase,
    private val getBannerUseCase: GetBannerListUseCase, // Assuming this is also product/category related

// Shopping Cart and Favorites Use Cases
    private val addToCardUseCase: AddToCartUseCase, // Typo in original, assuming "addToCartUseCase" is intended
    private val getCartUseCase: GetCartUseCase,
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val getAllWishListUseCase: GetWishlistUseCase,

    private val addToOrderUseCase: AddToOrderUseCase,

    private val searchProductStateUseCase: SearchProductByNameUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val incrementCartQuantityUseCase: IncrementCartQuantityUseCase,
    private val reduceCartQuantityUseCase: ReduceOneCartQuantityUseCase,
    private val getOrderListUseCase: GetOrderListUseCase,
    private val removeWishListItemUseCase: RemoveItemFromWishListUseCase,

    private val addShippingDetailsUseCase: AddShippingDetailsUseCase,
    private val getShippingDetailListUseCase: GetShippingDetailListUseCase,
    private val getVariantListUseCase: GetVariantListUseCase

) : ViewModel() {

    private val _signUpScreenState = MutableStateFlow(SignUpScreenState())
    val signUpScreenState = _signUpScreenState.asStateFlow()

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()

    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState = _profileScreenState.asStateFlow()


    private val _updateScreenState = MutableStateFlow(UpdateScreenState())
    val updateScreenState = _updateScreenState.asStateFlow()




    private val _addToCartState = MutableStateFlow(AddToCartState())
    val addToCartState = _addToCartState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()

    private val _addToFavState = MutableStateFlow(AddToWishListState())
    val addToFavState = _addToFavState.asStateFlow()

    private val _getAllFavState = MutableStateFlow(GetAllWishlistState())
    val getAllFavState = _getAllFavState.asStateFlow()


    private val _getAllProductState = MutableStateFlow(GetAllProductState())
    val getAllProductState = _getAllProductState.asStateFlow()

    private val _getCartState = MutableStateFlow(GetCartState())
    val getCartState = _getCartState.asStateFlow()

    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState = _getAllCategoriesState.asStateFlow()

    private val _getCheckoutState = MutableStateFlow(GetCheckoutState())
    val getCheckoutState = _getCheckoutState.asStateFlow()

    private val _getSpecificCategoryItemsState = MutableStateFlow(GetSpecificCategoryItemsState())
    val getSpecificCategoryItemsState = _getSpecificCategoryItemsState.asStateFlow()

    private val _addToOrdersState= MutableStateFlow(AddToOrdersState())

    private val _getAllSuggestedProductState = MutableStateFlow(GetAllSuggestedProductState())
    val getAllSuggestedProductState = _getAllSuggestedProductState.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState_2())
    val homeScreenState = _homeScreenState.asStateFlow()

    private val _searchProductState=MutableStateFlow(SearchProductByNameState())
    val searchProductState=_searchProductState.asStateFlow()

    private val _getOrderListState= MutableStateFlow(GetOrderListState())
    val getOrderListState=_getOrderListState.asStateFlow()

    private val _incrementCartQuantityState= MutableStateFlow(IncrementCartQuantityState())
    val incrementCartQuantityState=_incrementCartQuantityState.asStateFlow()

    private val _decrementCartQuantityState= MutableStateFlow(DecrementCartQuantityState())
    val decrementCartQuantityState=_decrementCartQuantityState.asStateFlow()

    private val _clearCartState= MutableStateFlow(ClearCartState())
    val clearCartState=_clearCartState.asStateFlow()

    private val _removeWishlistItemState= MutableStateFlow(RemoveWishListItemState())
    val removeWishListItemState=_removeWishlistItemState.asStateFlow()

    private val _loadingQuantityMap = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val loadingQuantityMap: State<Map<String, Boolean>> = _loadingQuantityMap

    private val _addShippingDetailsState= MutableStateFlow(AddShippingDetailsState())
    val addShippingDetailsState=_addShippingDetailsState.asStateFlow()

    private val _getShippingDetailsState = MutableStateFlow(GetShippingDetailListState())
    val getShippingDetailListState= _getShippingDetailsState.asStateFlow()

    private val _totalCartItemAmount=mutableStateOf(0)
    val totalCartItemAmount:State<Int> = _totalCartItemAmount



    fun getSpecificCategoryItems(categoryId: String) {
        viewModelScope.launch {
            getSpecificCategoryItemsUseCase.getSpecificCategoryItems(categoryId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getSpecificCategoryItemsState.value =
                            _getSpecificCategoryItemsState.value.copy(
                                isLoading = true,
                            )
                    }

                    is ResultState.Error -> {
                        _getSpecificCategoryItemsState.value =
                            _getSpecificCategoryItemsState.value.copy(
                                isLoading = false,
                                errorMessage = it.error
                            )
                    }

                    is ResultState.Success -> {
                        _getSpecificCategoryItemsState.value =
                            _getSpecificCategoryItemsState.value.copy(
                                isLoading = false,
                                userData = it.result
                            )
                    }
                }
            }
        }
    }





    fun searchProductsByName(query: String) {
        viewModelScope.launch {
            searchProductStateUseCase.searchProductByNameUseCase(query).collect {
                when(it){
                    is ResultState.Loading -> {
                        _searchProductState.value=_searchProductState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success->{
                        _searchProductState.value=_searchProductState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                    is ResultState.Error->{
                        _searchProductState.value=_searchProductState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                }
            }
        }
    }

    fun getOrderList(){
        viewModelScope.launch {
            getOrderListUseCase.getOrderList().collect {
                when(it){
                    is ResultState.Loading -> {
                        _getOrderListState.value=_getOrderListState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success->{
                        _getOrderListState.value=_getOrderListState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                    is ResultState.Error->{
                        _getOrderListState.value=_getOrderListState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                }
            }
        }
    }

    fun incrementCartQuantity(cartItemModel: CartItemModel){
        viewModelScope.launch {
            _loadingQuantityMap.value=_loadingQuantityMap.value + (cartItemModel.cartItemId to true)
            incrementCartQuantityUseCase.incrementCartQuantity(cartItemModel).collect {
                when(it){
                    is ResultState.Loading -> {

                    }
                    is ResultState.Success->{
                        updateCartInMemory(cartItemModel.copy(quantity = cartItemModel.quantity+1))

                        _loadingQuantityMap.value=_loadingQuantityMap.value.toMutableMap().apply {
                            this[cartItemModel.cartItemId] = false
                        }
                    }
                    is ResultState.Error->{
                        _loadingQuantityMap.value=_loadingQuantityMap.value.toMutableMap().apply {
                            this[cartItemModel.cartItemId] = false
                        }

                    }
                }
            }


        }
    }

    fun updateCartInMemory(cartItemModel: CartItemModel){
        val currentCartList=_getCartState.value.userData
        val updatedCart=currentCartList.map {
            if (it?.cartItemId==cartItemModel.cartItemId)cartItemModel else it
        }
        _getCartState.value=_getCartState.value.copy(
            userData = updatedCart
        )
        _totalCartItemAmount.value=0
        updatedCart.forEach { cartItem->
            _totalCartItemAmount.value +=cartItem!!.quantity *cartItem.price.toInt()

        }
    }


    fun decrementCartQuantity(cartItemModel: CartItemModel){

        viewModelScope.launch {
            _loadingQuantityMap.value=_loadingQuantityMap.value + (cartItemModel.cartItemId to true)

            reduceCartQuantityUseCase.reduceOneCartQuantity(cartItemModel).collect {
                when(it){
                    is ResultState.Loading -> {
                        _decrementCartQuantityState.value=_decrementCartQuantityState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success->{
                        updateCartInMemory(cartItemModel.copy(quantity = cartItemModel.quantity-1))

                        _loadingQuantityMap.value=_loadingQuantityMap.value.toMutableMap().apply {
                            this[cartItemModel.cartItemId] = false
                        }
                    }
                    is ResultState.Error->{

                    }
                }
            }

        }
    }

    fun clearCart(){
        viewModelScope.launch {
            clearCartUseCase.clearCart().collect {
                when(it){
                    is ResultState.Loading -> {
                        _clearCartState.value=_clearCartState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success->{
                        _clearCartState.value=_clearCartState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                    is ResultState.Error->{
                        _clearCartState.value=_clearCartState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                }
            }
        }
    }

    fun removeWishListItem(wishListItemModel: WishListItemModel){
        viewModelScope.launch {
            removeWishListItemUseCase.removeItemFromWishlist(wishListItemModel).collect {
                when(it){
                    is ResultState.Loading -> {
                        _removeWishlistItemState.value=_removeWishlistItemState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success->{
                        _removeWishlistItemState.value=_removeWishlistItemState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                    is ResultState.Error->{
                        _removeWishlistItemState.value=_removeWishlistItemState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                }
            }
        }
    }






    fun addToOrders(ordersData: OrdersData){
        viewModelScope.launch {
            addToOrderUseCase.addToOrderUseCase(ordersData).collect {
                when(it){
                    is ResultState.Loading -> {
                        _addToOrdersState.value=_addToOrdersState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Error -> {
                        _addToOrdersState.value=_addToOrdersState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                    is ResultState.Success ->{
                        _addToOrdersState.value=_addToOrdersState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                }
            }
        }
    }

    fun getCheckOut(productId: String) {
        viewModelScope.launch {
            getCheckoutUseCase.getCheckout(productId).collect {
                when (it) {
                    is ResultState.Success -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }

                    is ResultState.Error -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }

                    is ResultState.Loading -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase.getAllCategoryList().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Error -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }

                    is ResultState.Success -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }


                }
            }
        }
    }


    fun getCart() {
        viewModelScope.launch {
            getCartUseCase.getCart().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Error -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }

                    is ResultState.Success -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                        it.result?.forEach {cartItem->

                            _totalCartItemAmount.value +=cartItem.quantity *cartItem.price.toInt()

                        }
                    }
                }

            }
        }
    }


    fun addShippingDetails(shippingDetails: ShippingDetails) {
        Log.d("Firestore shipping debug", "addShippingDetails: calling from viewModel")
        viewModelScope.launch {
            addShippingDetailsUseCase.addShippingDetails(shippingDetails).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addShippingDetailsState.value=_addShippingDetailsState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Error -> {
                        _addShippingDetailsState.value=_addShippingDetailsState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }

                    is ResultState.Success -> {
                        _addShippingDetailsState.value=_addShippingDetailsState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )

                    }
                }

            }
        }
    }


    fun getAllProducts() {
        viewModelScope.launch {
            Log.d("search", "ShopViewModel: about to search")

            getAllProductsUseCase.getAllProducts().collect {
                Log.d("search", "ShopViewModel: inside collect- looking for result")

                when (it) {


                    is ResultState.Success -> {
                        Log.d("search", "ShopViewModel: got result --- ${it} ---- ${it.result}")

                        _getAllProductState.value = _getAllProductState.value.copy(

                            isLoading = false,
                            userData = it.result
                        )
                        Log.d("search", "ShopViewModel: successfully updated the result")

                    }

                    is ResultState.Error -> {
                        Log.d("search", "ShopViewModel: error--- ${it} ---- ${it.error}")

                        _getAllProductState.value = _getAllProductState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }

                    is ResultState.Loading -> {
                        Log.d("search", "ShopViewModel: loading")
                        _getAllProductState.value = _getAllProductState.value.copy(
                            isLoading = true
                        )
                    }
                }

            }
        }

    }


    fun getAllFav() {
        viewModelScope.launch {
            getAllWishListUseCase.getWishlist().collect {
                when (it) {

                    is ResultState.Success -> {
                        _getAllFavState.value = _getAllFavState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }

                    is ResultState.Error -> {
                        _getAllFavState.value = _getAllFavState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }

                    is ResultState.Loading -> {
                        _getAllFavState.value = _getAllFavState.value.copy(
                            isLoading = true
                        )
                    }

                }

            }

        }


    }


    fun addToFav(wishListState: WishListItemModel) {
        viewModelScope.launch {
            addToWishlistUseCase.addToWishList(wishListState).collect {

                when (it) {
                    is ResultState.Success -> {
                        _addToFavState.value = _addToFavState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )

                    }

                    is ResultState.Error -> {
                        _addToFavState.value = _addToFavState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }

                    is ResultState.Loading -> {
                        _addToFavState.value = _addToFavState.value.copy(
                            isLoading = true
                        )
                    }


                }
            }

        }
    }

    fun getProductById(productId: String) {
        Log.d("Firebase", "getProductById: success ")
        viewModelScope.launch {
            Log.d("Firebase", "getProductById: inside launch")

            combine (

                getProductByIDUseCase.getProductById(productId),
                getVariantListUseCase.getVariantList(productId)

            ) { getProductResult, getVariantResult ->
                Log.d("Firebase", "getProductById: inside combine - ${getProductResult}  vairant - ${getVariantResult} ")
                when {
                    getProductResult is ResultState.Error -> {
                        GetProductByIdState(
                            isLoading = false,
                            errorMessage =getProductResult.error
                        )
                    }

                    getVariantResult is ResultState.Error -> {
                        GetProductByIdState(
                            isLoading = false,
                            errorMessage = getVariantResult.error
                        )
                    }

                    getVariantResult is ResultState.Success && getProductResult is ResultState.Success -> {
                        Log.d("Firestore", "getProductById: product ${getProductResult.result}\n variant: ${getVariantResult.result}")

                        GetProductByIdState(
                            isLoading = false,
                            productData = getProductResult.result,
                            variantDataList = getVariantResult.result
                        )
                    }
                    else->{
                        GetProductByIdState(
                                isLoading = true
                            )
                    }
                }
            }.collect {
                state->_getProductByIdState.value=state
            }
        }
    }

    fun addToCart(cartDataModel: CartItemModel) {
        viewModelScope.launch {
            addToCardUseCase.addToCart(cartDataModel).collect {
                when (it) {
                    is ResultState.Success -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }

                    is ResultState.Error -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )

                    }

                    is ResultState.Loading -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = true
                        )
                    }
                }

            }
        }
    }

    fun loadHomeScreen() {
        viewModelScope.launch {
            combine(
                getProductsInLimitUseCase.getProductsInLimit(),
                getCategoryInLimitUseCase.getCategoryInLimit(),
                getBannerUseCase.getBannerList(),
            ) { productResult, categoryResult, bannersResult ->

                when {
                    categoryResult is ResultState.Error -> {
                        HomeScreenState_2(
                            isLoading = false,
                            errorMessage = categoryResult.error
                        )
                    }

                    productResult is ResultState.Error -> {
                        HomeScreenState_2(
                            isLoading = false,
                            errorMessage = productResult.error
                        )
                    }

                    bannersResult is ResultState.Error -> {
                        HomeScreenState_2(
                            isLoading = false,
                            errorMessage = bannersResult.error
                        )
                    }

                    categoryResult is ResultState.Success &&
                            productResult is ResultState.Success &&
                            bannersResult is ResultState.Success -> {
                        HomeScreenState_2(
                            categories = categoryResult.result,
                            products = productResult.result,
                            banner = bannersResult.result,
                            isLoading = false
                        )
                    }

                    else -> {
                        HomeScreenState_2(
                            isLoading = true
                        )
                    }

                }

            }
                .collect {
                    state->_homeScreenState.value=state
                }
        }
    }



    fun updateUserData(userData: UserData) {
        viewModelScope.launch {

            upDateUserDataUseCase.updateUserData(userData = userData).collect{
                when(it){
                    is ResultState.Success-> {
                        _updateScreenState.value = _updateScreenState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                    is ResultState.Error-> {
                        _updateScreenState.value = _updateScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                    is ResultState.Loading ->{
                        _updateScreenState.value = _updateScreenState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }

    }


    fun createUser(userData: UserData, password: String){
        viewModelScope.launch {
            createUserUseCase.createUser(userData, password).collect {
                when (it) {

                    is ResultState.Success -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                    is ResultState.Error -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                    is ResultState.Loading -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            isLoading = true
                        )
                    }

                }

            }
        }
    }


    fun loginUser(userData: UserData, password: String){
        viewModelScope.launch {

            loginUserUseCase.loginUser(userData, password).collect {
                when (it) {
                    is ResultState.Success -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                    is ResultState.Error -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                    is ResultState.Loading -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }

    }

    fun getUserById(uid: String) {
        viewModelScope.launch {
            getUserUseCase.getUserById(uid).collect {
                when(it){
                    is ResultState.Success-> {
                        _profileScreenState.value = _profileScreenState.value.copy(
                            isLoading = false,
                            userData = it.result
                        )
                    }
                    is ResultState.Error-> {
                        _profileScreenState.value = _profileScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.error
                        )
                    }
                    is ResultState.Loading ->{
                        _profileScreenState.value = _profileScreenState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }

    }

    fun getAllSuggestedProducts(){
        viewModelScope.launch {
            getAllSuggestedProductsUseCase.getAllSuggestedProductList().collect{

                when(it){
                    is ResultState.Success-> {
                        _getAllSuggestedProductState.value =
                            _getAllSuggestedProductState.value.copy(
                                isLoading = false,
                                userData = it.result
                            )
                    }
                    is ResultState.Error-> {
                        _getAllSuggestedProductState.value =
                            _getAllSuggestedProductState.value.copy(
                                isLoading = false,
                                errorMessage = it.error
                            )
                    }
                    is ResultState.Loading ->{
                        _getAllSuggestedProductState.value =
                            _getAllSuggestedProductState.value.copy(
                                isLoading = true
                            )
                    }
                }

            }

        }
    }

    fun getShippingDetailList(){


        viewModelScope.launch {
            getShippingDetailListUseCase.getShippingDetailList().collect{

                when(it){
                    is ResultState.Success-> {
                        _getShippingDetailsState.value= _getShippingDetailsState.value.copy(
                                isLoading = false,
                                userData = it.result
                            )
                    }
                    is ResultState.Error-> {
                        _getShippingDetailsState.value= _getShippingDetailsState.value.copy(
                            isLoading = false,
                                errorMessage = it.error
                            )
                    }
                    is ResultState.Loading ->{
                        _getShippingDetailsState.value= _getShippingDetailsState.value.copy(
                            isLoading = true
                            )
                    }
                }

            }

        }
    }




    fun filterProducts(products: List<ProductsDataModel?>, query: String): List<ProductsDataModel?> {
        return products.filter {
            it?.name?.contains(query.trim(), ignoreCase = true) == true
        }
    }
    fun filterWishlistItems(items: List<WishListItemModel?>, query: String): List<WishListItemModel?> {
        return items.filter {
            it?.name?.contains(query.trim(), ignoreCase = true) == true
        }
    }





    init {
        loadHomeScreen()
    }


}
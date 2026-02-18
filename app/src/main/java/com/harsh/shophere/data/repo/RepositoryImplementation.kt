package com.harsh.shophere.data.repo

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.harsh.shophere.common.BANNER_COLLECTION_2
import com.harsh.shophere.common.CART_COLLECTION_2
import com.harsh.shophere.common.CATEGORIES_COLLECTION_2
import com.harsh.shophere.common.ORDER_COLLECTION
import com.harsh.shophere.common.PRODUCT_COLLECTION_2
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.common.USER_COLLECTION_2
import com.harsh.shophere.common.VARIANTS_COLLECTION
import com.harsh.shophere.common.WISHLIST_COLLECTION_2
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
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import javax.inject.Inject

class RepositoryImplementation @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : Repository {
    override fun registerUserWithEmailAndPassword(
        userData: UserData,
        password: String
    ): Flow<ResultState<String>> =
        callbackFlow { // 2- 6:03
            trySend(ResultState.Loading)
            firebaseAuth.createUserWithEmailAndPassword(userData.email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseFirestore
                            .collection(USER_COLLECTION_2)
                            .document(it.result.user!!.uid)
                            .set(userData.copy(userId = it.result.user!!.uid))
                            .addOnSuccessListener {
                                trySend(ResultState.Success("User is registered and saved successfully"))
                            }
                            .addOnFailureListener {
                                trySend(
                                    ResultState.Error(
                                        it.localizedMessage ?: "Something Went Wrong"
                                    )
                                )
                            }
                    } else {
                        if (it.exception != null) {
                            trySend(
                                ResultState.Error(
                                    it.exception?.localizedMessage ?: "Failed to create user."
                                )
                            )
                        }
                    }
                }
            awaitClose {}
        }

    override fun loginUserWithEmailAndPassWord(
        userData: UserData,
        password: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseAuth.signInWithEmailAndPassword(userData.email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("User Login Successfully"))
                } else {
                    trySend(
                        ResultState.Error(
                            it.exception?.localizedMessage ?: "Something Went Wrong"
                        )
                    )
                }
            }
        awaitClose {}
    }

    override fun getUserById(uid: String): Flow<ResultState<UserDataParent>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result.toObject(UserData::class.java)
                    if (data != null) {

                        val userDataParent = UserDataParent(it.result.id, data)
                        trySend(ResultState.Success(userDataParent))
                    } else {
                        trySend(ResultState.Error("User Data is Null or couldn't be parsed"))
                    }
                } else {
                    trySend(
                        ResultState.Error(
                            it.exception?.localizedMessage
                                ?: "Some Error Occurred While Fetching Data"
                        )
                    )
                }
            }
        awaitClose {}
    }

    override fun updateUserData(userData: UserData): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d("Firestore", "updateUserData: image uri- ${userData.profileImage}")
        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(userData.userId)
            .update(userData.toMap())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("User Data Updated Successfully"))
                } else {
                    trySend(
                        ResultState.Error(
                            it.exception?.localizedMessage ?: "Something Went Wrong"
                        )
                    )
                }
            }
        awaitClose {}
    }


    override fun getCategoriesInLimit(): Flow<ResultState<List<CategoryDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(CATEGORIES_COLLECTION_2)
            .limit(9)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val categories = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(CategoryDataModel::class.java)
                        ?.apply {
                            categoryId = document.id
                        }
                }
                trySend(ResultState.Success(categories))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to Fetch Products"))
            }
        awaitClose {}
    }

    override fun getProductsInLimit(): Flow<ResultState<List<ProductsDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(PRODUCT_COLLECTION_2)
            .limit(10)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val products = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(ProductsDataModel::class.java)
                        ?.apply {
                            productId = document.id
                        }

                }
                trySend(ResultState.Success(products))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to Fetch Products"))
            }
        awaitClose {}
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductsDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore
            .collection(PRODUCT_COLLECTION_2)
            .get()
            .addOnSuccessListener {
                Log.d("search", "RespositoryImplementation: success got product list --- ${it}")

                val products = it.documents.mapNotNull { product ->
                    product.toObject(ProductsDataModel::class.java)
                        ?.apply {
                            productId = product.id
                        }

                }
                Log.d(
                    "search",
                    "RespositoryImplementation: success got product list --- ${products}"
                )

                trySend(ResultState.Success(products))
                Log.d("search", "RespositoryImplementation: Successfully sent the products")

            }
            .addOnFailureListener {
                Log.d("search", "RespositoryImplementation: failed")

                trySend(ResultState.Error(it.localizedMessage ?: "Failed to Fetch Products"))
            }
        awaitClose()
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductsDataModel>> =
        callbackFlow {
            Log.d("Firebase", "getProductById: repoImpl")
            trySend(ResultState.Loading)
            firebaseFirestore.collection(PRODUCT_COLLECTION_2)
                .document(productId)
                .get()
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Failed to Fetch Product"))
                    Log.d("Firebase", "getProductById: repoImpl - fail ${it.localizedMessage}")

                }
                .addOnSuccessListener {
                    val product = it.toObject(ProductsDataModel::class.java)
                    if (product != null) {
                        trySend(ResultState.Success(product))
                    }

                    Log.d("Firebase", "getProductById: repoImpl - success ${product}")

                }
            awaitClose()
        }

    override fun addToCart(cartDataModel: CartItemModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User not Logged in"))
            close()
            return@callbackFlow
        }


        val docId =
            "${cartDataModel.productId}_${cartDataModel.variantId}_${cartDataModel.selectedOptions.keys.first()}"

        val docRef = firebaseFirestore.collection(USER_COLLECTION_2)
            .document(uid)
            .collection(CART_COLLECTION_2)
            .document(docId)

        docRef.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val cartItem = it.toObject(CartItemModel::class.java)
                    val newQuantity = (cartItem?.quantity ?: 1) + cartDataModel.quantity
                    docRef.update("quantity", newQuantity)
                        .addOnSuccessListener {
                            trySend(ResultState.Success("Cart quantity updated"))
                        }
                        .addOnFailureListener {
                            trySend(
                                ResultState.Error(
                                    it.localizedMessage ?: "Failed to update cart"
                                )
                            )
                        }
                } else {
                    docRef.set(cartDataModel.copy(cartItemId = docId))
                        .addOnSuccessListener {
                            trySend(ResultState.Success("Product added to cart"))
                        }
                        .addOnFailureListener {
                            trySend(
                                ResultState.Error(
                                    it.localizedMessage ?: "Failed to add to cart"
                                )
                            )
                        }
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to get Cart"))
            }

        awaitClose {}
    }

    override fun addToWishlist(wishListItemModel: WishListItemModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            val uid = firebaseAuth.currentUser?.uid ?: run { /*TODO-Learn about this*/
                trySend(ResultState.Error("User is not logged in"))
                close()
                return@callbackFlow
            }

            firebaseFirestore
                .collection(USER_COLLECTION_2)
                .document(uid)
                .collection(WISHLIST_COLLECTION_2)
                .document(wishListItemModel.productId)
                .set(wishListItemModel.copy(wishListItemId = wishListItemModel.productId))
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("Added to the wishlist"))
                    } else {
                        trySend(
                            ResultState.Error(
                                it.exception?.localizedMessage ?: "Failed to add in wishlist"
                            )
                        )
                    }
                }
            awaitClose {}


        }

    override fun getWishlist(): Flow<ResultState<List<WishListItemModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(uid)
            .collection(WISHLIST_COLLECTION_2)
            .orderBy("addedAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val wishlist = it.documents.mapNotNull { document ->
                    document.toObject(WishListItemModel::class.java)
                        ?.copy(wishListItemId = document.id)
                }
                trySend(ResultState.Success(wishlist))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch wishlist"))

            }

        awaitClose()

    }

    override fun getCart(): Flow<ResultState<List<CartItemModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore
            .collection(USER_COLLECTION_2)
            .document(uid)
            .collection(CART_COLLECTION_2)
            .orderBy("addedAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val cart = it.documents.mapNotNull { document ->
                    document.toObject(CartItemModel::class.java)
                        ?.copy(cartItemId = document.id)
                }
                trySend(ResultState.Success(cart))

            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch Cart"))

            }

        awaitClose {}
    }

    override fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(CATEGORIES_COLLECTION_2)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val categories = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(CategoryDataModel::class.java)
                }
                trySend(ResultState.Success(categories))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to Fetch Products"))
            }
        awaitClose {}


    }

    override fun getCheckout(productId: String): Flow<ResultState<ProductsDataModel>> =
        callbackFlow {
            /*TODO-Implement checkout*/
        }

    override fun getBanners(): Flow<ResultState<List<BannerDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(BANNER_COLLECTION_2)
            .get()
            .addOnSuccessListener {
                val banners = it.documents.mapNotNull { document ->
                    document.toObject(BannerDataModel::class.java)
                }
                trySend(ResultState.Success(banners))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to Fetch Banners"))
            }

        awaitClose {}
    }

    override fun getSpecificCategoryItems(categoryId: String): Flow<ResultState<List<ProductsDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(PRODUCT_COLLECTION_2)
                .whereEqualTo("categoryId", categoryId)
                .get()
                .addOnSuccessListener {
                    val productsInCategory = it.documents.mapNotNull { document ->
                        document.toObject(ProductsDataModel::class.java)?.copy(productId = document.id)
                    }
                    trySend(ResultState.Success(productsInCategory))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch Products"))
                }
            awaitClose {}
        }

    override fun getAllSuggestedProducts(): Flow<ResultState<List<ProductsDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            val uid = firebaseAuth.currentUser?.uid ?: run {
                trySend(ResultState.Error("User is not logged in"))
                close()
                return@callbackFlow
            }

            firebaseFirestore.collection(USER_COLLECTION_2)
                .document(uid)
                .collection(WISHLIST_COLLECTION_2)
                .get()
                .addOnSuccessListener {
                    val wishlist = it.documents.mapNotNull { document ->
                        document.toObject(WishListItemModel::class.java)?.copy(productId = document.id)
                    }
                    val productList = wishlist.map { it.productId }
                    /*TODO- write the correct logic because we can't use
                    *  whereIn function with empty productList*/
                    if (!productList.isEmpty()) {
                        firebaseFirestore.collection(PRODUCT_COLLECTION_2)
                            .whereIn(FieldPath.documentId(), productList.take(10))
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val productList = snapshot.documents.mapNotNull { document ->
                                    document.toObject(ProductsDataModel::class.java)
                                }
                                trySend(ResultState.Success(productList))
                            }
                            .addOnFailureListener {
                                trySend(
                                    ResultState.Error(
                                        it.localizedMessage ?: "Failed to Fetch Products"
                                    )
                                )
                            }
                    } else {
                        trySend(ResultState.Error("No Products to suggest."))
                    }


                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch wishlist"))

                }

            awaitClose()

        }


    override fun addToOrder(ordersData: OrdersData): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(ORDER_COLLECTION)
            .add(ordersData.copy(userId = firebaseAuth.currentUser!!.uid))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("Order Added Successfully"))
                } else {
                    trySend(
                        ResultState.Error(
                            it.exception?.localizedMessage ?: "Failed to add Order"
                        )
                    )
                }
            }
        awaitClose()
    }


    override fun getOrderList(): Flow<ResultState<List<OrdersData>>> = callbackFlow {
        trySend(ResultState.Loading)
        val userId = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }


        firebaseFirestore.collection(ORDER_COLLECTION)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val orderList = snapshot.documents.mapNotNull { document ->
                    document.toObject(OrdersData::class.java)
                        ?.copy(orderId = document.id)
                }
                trySend(ResultState.Success(orderList))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Fail to fetch orders."))
            }

        awaitClose {}
    }

    override fun addOneQuantityIntoTheCartItem(cartItemModel: CartItemModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            val userId = firebaseAuth.currentUser?.uid ?: run {
                trySend(ResultState.Error("User is not logged in"))
                close()
                return@callbackFlow
            }

            firebaseFirestore.collection(USER_COLLECTION_2)
                .document(userId)
                .collection(CART_COLLECTION_2)
                .document(cartItemModel.cartItemId)
                .update("quantity", cartItemModel.quantity + 1)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("Quantity increased successfully"))
                    } else {
                        trySend(ResultState.Error("Failed to increase quantity"))
                    }
                }

            awaitClose {}


        }

    override fun reduceOneQuantityFromCartItem(cartItemModel: CartItemModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            val userId = firebaseAuth.currentUser?.uid ?: run {
                trySend(ResultState.Error("User is not logged in"))
                close()
                return@callbackFlow
            }

            firebaseFirestore.collection(USER_COLLECTION_2)
                .document(userId)
                .collection(CART_COLLECTION_2)
                .document(cartItemModel.cartItemId)
                .update("quantity", cartItemModel.quantity - 1)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("Quantity reduced successfully"))
                    } else {
                        trySend(ResultState.Error("Failed to reduce quantity"))
                    }
                }

            awaitClose {}


        }

    override fun removeItemFromWishlist(wishListItemModel: WishListItemModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            val userId = firebaseAuth.currentUser?.uid ?: run {
                trySend(ResultState.Error("User is not logged in"))
                close()
                return@callbackFlow
            }

            firebaseFirestore.collection(USER_COLLECTION_2)
                .document(userId)
                .collection(WISHLIST_COLLECTION_2)
                .document(wishListItemModel.wishListItemId)
                .delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("Wishlist item deleted successfully"))
                    } else {
                        trySend(ResultState.Error("Failed to Wishlist item: ${wishListItemModel.name}"))
                    }
                }

            awaitClose {}


        }

    override fun clearAllCartItems(): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val userId = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(userId)
            .collection(CART_COLLECTION_2)
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = firebaseFirestore.batch()
                snapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)

                }
                batch.commit()
                    .addOnSuccessListener {
                        trySend(ResultState.Success("Cart cleared successfully"))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error("Failed to clear cart"))
                    }
            }
            .addOnFailureListener {
                trySend(ResultState.Error("Failed to get and clear cart"))
            }
        awaitClose {}
    }

    override fun searchProductInMainScreen(query: String): Flow<ResultState<List<ProductsDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(PRODUCT_COLLECTION_2)
                .orderBy("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .addOnSuccessListener { snapshot ->
                    val productList = snapshot.documents.mapNotNull { document ->
                        document.toObject(ProductsDataModel::class.java)?.apply {
                            productId = document.id
                        }
                    }
                    trySend(ResultState.Success(productList))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Search failed"))
                }

            awaitClose {}
        }

    override fun searchWishListByName(name: String): Flow<ResultState<List<WishListItemModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(WISHLIST_COLLECTION_2)
                .orderBy("name")
                .startAt(name)
                .endAt(name + "\uf8ff")
                .get()
                .addOnSuccessListener { snapshot ->
                    val wishlist = snapshot.documents.mapNotNull { document ->
                        document.toObject(WishListItemModel::class.java)?.apply {
                            wishListItemId = document.id
                        }
                    }
                    trySend(ResultState.Success(wishlist))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Search failed"))
                }

            awaitClose {}
        }

    override fun searchCategoriesByName(name: String): Flow<ResultState<List<CategoryDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(CATEGORIES_COLLECTION_2)
                .orderBy("name")
                .startAt(name)
                .endAt(name + "\uf8ff")
                .get()
                .addOnSuccessListener { snapshot ->
                    val categoryList = snapshot.documents.mapNotNull { document ->
                        document.toObject(CategoryDataModel::class.java)?.apply {
                            categoryId = document.id
                        }
                    }
                    trySend(ResultState.Success(categoryList))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Search failed"))
                }

            awaitClose {}
        }

    override fun addShippingDetails(
        shippingDetails: ShippingDetails,
    ): Flow<ResultState<String>> = callbackFlow {

        Log.d("Firestore shipping debug", "addShippingDetails: repo function called")
        trySend(ResultState.Loading)

        val userId = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        val newShippingId = UUID.randomUUID().toString()

        val userDocRef = firebaseFirestore.collection(USER_COLLECTION_2).document(userId)

        userDocRef.get()
            .addOnSuccessListener { snapshot ->
                Log.d("Firestore shipping debug", "addShippingDetails: repoImp: ")
                val currentUser = snapshot.toObject(UserData::class.java)
                Log.d(
                    "Firestore shipping debug",
                    "addShippingDetails: repoImp: got current user data $currentUser"
                )


                if (currentUser != null) {
                    val updatedList = currentUser.shippingDetails.toMutableList()
                    updatedList.add(
                        shippingDetails.copy(
                            shippingDetailsId = newShippingId
                        )
                    )
                    Log.d(
                        "Firestore shipping debug",
                        "addShippingDetails: repoImp: list updated $updatedList"
                    )


                    userDocRef.update("shippingDetails", updatedList)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
                                trySend(ResultState.Success("Shipping details saved successfully"))
                                Log.d(
                                    "Firestore shipping debug",
                                    "addShippingDetails: repoImp: successfully updated"
                                )

                            } else {
                                trySend(
                                    ResultState.Error(
                                        it.exception?.localizedMessage
                                            ?: "Failed to update shipping details"
                                    )
                                )
                                Log.d(
                                    "Firestore shipping debug",
                                    "addShippingDetails: repoImp: error${it.exception?.localizedMessage}"
                                )

                            }
                        }
                } else {
                    Log.d(
                        "Firestore shipping debug",
                        "addShippingDetails: repoImp: User data not found"
                    )

                    trySend(ResultState.Error("User data not found"))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch user data"))
            }

        awaitClose {}


    }

    override fun getShippingDetailList(): Flow<ResultState<List<ShippingDetails>>> = callbackFlow {
        trySend(ResultState.Loading)

        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        Log.d("Firestore Get", "getShippingDetailList: trying to get uid${uid}")
        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("Firestore Get", "getShippingDetailList: inside success")


                var userData = snapshot.toObject(UserData::class.java)
                if (userData != null) {
                    val shippingDetailList = userData.shippingDetails
                    trySend(ResultState.Success(shippingDetailList))
                } else {
                    Log.d("Firestore Get", "getShippingDetailList: null user data")
                    trySend(ResultState.Error("User data is null"))
                }

            }
            .addOnFailureListener {
                Log.d("Firestore Get", "getShippingDetailList: failed:${it.localizedMessage}")
                trySend(ResultState.Error("Failed to get shipping List"))
            }
        awaitClose()

    }


    override fun getVariantList(productId: String): Flow<ResultState<List<VariantsDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            Log.d("Firebase", "getVariant: repoImpl - ")

            firebaseFirestore.collection(PRODUCT_COLLECTION_2)
                .document(productId)
                .collection(VARIANTS_COLLECTION)
                .get()
                .addOnSuccessListener { snapshot ->
                    var variantList = snapshot.documents.mapNotNull { document ->
                        document.toObject(VariantsDataModel::class.java)?.copy(
                            variantId = document.id
                        )
                    }
                    trySend(ResultState.Success(variantList))
                    Log.d("Firebase", "getVariant: repoImpl - ")

                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch variants"))
                    Log.d("Firebase", "getVariant: repoImpl - ")

                }

            awaitClose {}
        }


}














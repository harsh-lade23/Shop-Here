package com.harsh.shophere.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.shophere.common.PRODUCT_COLLECTION_2
import com.harsh.shophere.common.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.harsh.shophere.common.USER_COLLECTION_2
import com.harsh.shophere.common.VARIANTS_COLLECTION
import com.harsh.shophere.common.WISHLIST_COLLECTION_2
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.models.VariantsDataModel
import com.harsh.shophere.domain.repository.ProductRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : ProductRepository {

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
                Log.d("search", "ProductRepositoryImpl: success got product list --- ${it}")

                val products = it.documents.mapNotNull { product ->
                    product.toObject(ProductsDataModel::class.java)
                        ?.apply {
                            productId = product.id
                        }

                }
                Log.d(
                    "search",
                    "ProductRepositoryImpl: success got product list --- ${products}"
                )

                trySend(ResultState.Success(products))
                Log.d("search", "ProductRepositoryImpl: Successfully sent the products")

            }
            .addOnFailureListener {
                Log.d("search", "ProductRepositoryImpl: failed")

                trySend(ResultState.Error(it.localizedMessage ?: "Failed to Fetch Products"))
            }
        awaitClose()
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductsDataModel>> =
        callbackFlow {
            Log.d("Firebase", "getProductById: ProductRepositoryImpl")
            trySend(ResultState.Loading)
            firebaseFirestore.collection(PRODUCT_COLLECTION_2)
                .document(productId)
                .get()
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Failed to Fetch Product"))
                    Log.d("Firebase", "getProductById: ProductRepositoryImpl - fail ${it.localizedMessage}")

                }
                .addOnSuccessListener {
                    val product = it.toObject(ProductsDataModel::class.java)
                    if (product != null) {
                        trySend(ResultState.Success(product))
                    }

                    Log.d("Firebase", "getProductById: ProductRepositoryImpl - success ${product}")

                }
            awaitClose()
        }

    override fun searchProducts(query: String): Flow<ResultState<List<ProductsDataModel>>> =
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

    override fun getVariantList(productId: String): Flow<ResultState<List<VariantsDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            Log.d("Firebase", "getVariant: ProductRepositoryImpl - ")

            firebaseFirestore.collection(PRODUCT_COLLECTION_2)
                .document(productId)
                .collection(VARIANTS_COLLECTION)
                .get()
                .addOnSuccessListener { snapshot ->
                    val variantList = snapshot.documents.mapNotNull { document ->
                        document.toObject(VariantsDataModel::class.java)?.copy(
                            variantId = document.id
                        )
                    }
                    trySend(ResultState.Success(variantList))
                    Log.d("Firebase", "getVariant: ProductRepositoryImpl - ")

                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch variants"))
                    Log.d("Firebase", "getVariant: ProductRepositoryImpl - ")

                }

            awaitClose {}
        }

    override fun getSuggestedProducts(): Flow<ResultState<List<ProductsDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
                trySend(ResultState.Error("User is not logged in"))
                close()
                return@callbackFlow
            }

            firebaseFirestore.collection(USER_COLLECTION_2)
                .document(uid)
                .collection(WISHLIST_COLLECTION_2)
                .get()
                .addOnSuccessListener { wishlistSnapshot ->
                    Log.d("Tracking Wishlist", "getSuggestedProducts: wishlistSnapshot -> $wishlistSnapshot")

                    val wishlistIds = wishlistSnapshot.documents.mapNotNull {
                        Log.d("Tracking Wishlist", "getSuggestedProducts: mapping -> ${it.id} -> $it")
                        it.id
                    }
                    if (wishlistIds.isEmpty()) {
                        Log.d("Tracking Wishlist", "getSuggestedProducts: wishlist is emtpy")
                        trySend(ResultState.Success(emptyList()))
                    } else {
                        firebaseFirestore.collection(PRODUCT_COLLECTION_2)
                            .whereIn(com.google.firebase.firestore.FieldPath.documentId(), wishlistIds.take(10))
                            .get()
                            .addOnSuccessListener { productSnapshot ->
                                val products = productSnapshot.documents.mapNotNull { doc ->
                                    doc.toObject(ProductsDataModel::class.java)?.apply { productId = doc.id }
                                }
                                Log.d("Tracking Wishlist", "getSuggestedProducts: send products -> $products")

                                trySend(ResultState.Success(products))
                            }
                            .addOnFailureListener {
                                Log.d("Tracking Wishlist", "getSuggestedProducts: failed -> ${it.localizedMessage}")
                                trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch suggested products"))
                            }
                    }
                }
                .addOnFailureListener {
                    Log.d("Tracking Wishlist", "getSuggestedProducts: failed -> ${it.localizedMessage}")
                    trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch wishlist"))
                }

            awaitClose()
        }
}

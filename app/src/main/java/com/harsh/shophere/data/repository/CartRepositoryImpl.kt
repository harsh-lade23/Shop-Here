package com.harsh.shophere.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.shophere.common.CART_COLLECTION_2
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.common.USER_COLLECTION_2
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.repository.CartRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : CartRepository {

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
            .orderBy("addedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
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

    override fun removeFromCart(cartItemId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val userId = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(userId)
            .collection(CART_COLLECTION_2)
            .document(cartItemId)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("Cart item removed successfully"))
                } else {
                    trySend(ResultState.Error("Failed to remove cart item"))
                }
            }

        awaitClose {}
    }

    override fun updateQuantity(cartItemId: String, quantity: Int): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val userId = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(userId)
            .collection(CART_COLLECTION_2)
            .document(cartItemId)
            .update("quantity", quantity)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("Quantity updated successfully"))
                } else {
                    trySend(ResultState.Error("Failed to update quantity"))
                }
            }

        awaitClose {}
    }

    override fun clearCart(): Flow<ResultState<String>> = callbackFlow {
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
}

package com.harsh.shophere.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.common.USER_COLLECTION_2
import com.harsh.shophere.common.WISHLIST_COLLECTION_2
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.domain.repository.WishlistRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : WishlistRepository {

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

    override fun addToWishlist(wishListItemModel: WishListItemModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val uid = firebaseAuth.currentUser?.uid ?: run {
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

    override fun removeFromWishlist(wishListItemId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(uid)
            .collection(WISHLIST_COLLECTION_2)
            .document(wishListItemId)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success("Wishlist item deleted successfully"))
                } else {
                    trySend(ResultState.Error("Failed to remove wishlist item"))
                }
            }

        awaitClose {}
    }

    override fun isInWishlist(productId: String): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(uid)
            .collection(WISHLIST_COLLECTION_2)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                trySend(ResultState.Success(document.exists()))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to check wishlist status"))
            }

        awaitClose {}
    }
}

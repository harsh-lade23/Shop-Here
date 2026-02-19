package com.harsh.shophere.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.common.USER_COLLECTION_2
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : UserRepository {

    override fun getUserById(uid: String): Flow<ResultState<UserData>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val userData = document.toObject(UserData::class.java)
                if (userData != null) {
                    trySend(ResultState.Success(userData))
                } else {
                    trySend(ResultState.Error("User not found"))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch user"))
            }

        awaitClose {}
    }

    override fun addShippingAddress(shippingDetails: ShippingDetails): Flow<ResultState<String>> = callbackFlow {
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
                val currentUser = snapshot.toObject(UserData::class.java)
                if (currentUser != null) {
                    val updatedList = currentUser.shippingDetails.toMutableList()
                    updatedList.add(shippingDetails.copy(shippingDetailsId = newShippingId))

                    userDocRef.update("shippingDetails", updatedList)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                trySend(ResultState.Success("Shipping details saved successfully"))
                            } else {
                                trySend(
                                    ResultState.Error(
                                        it.exception?.localizedMessage ?: "Failed to update shipping details"
                                    )
                                )
                            }
                        }
                } else {
                    trySend(ResultState.Error("User data not found"))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to fetch user data"))
            }

        awaitClose {}
    }

    override fun getShippingAddresses(): Flow<ResultState<List<ShippingDetails>>> = callbackFlow {
        trySend(ResultState.Loading)

        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val userData = snapshot.toObject(UserData::class.java)
                if (userData != null) {
                    trySend(ResultState.Success(userData.shippingDetails))
                } else {
                    trySend(ResultState.Error("User data is null"))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage ?: "Failed to get shipping List"))
            }

        awaitClose()
    }

    override fun updateUserProfile(userData: UserData): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val userId = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }

        firebaseFirestore.collection(USER_COLLECTION_2)
            .document(userId)
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
}

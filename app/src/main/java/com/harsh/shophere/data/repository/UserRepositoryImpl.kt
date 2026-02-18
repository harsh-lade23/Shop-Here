package com.harsh.shophere.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.common.USER_COLLECTION_2
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
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
}

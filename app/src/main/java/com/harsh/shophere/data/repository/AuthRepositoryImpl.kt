package com.harsh.shophere.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.common.USER_COLLECTION_2
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : AuthRepository {

    override fun registerUserWithEmailAndPassword(
        userData: UserData,
        password: String
    ): Flow<ResultState<String>> = callbackFlow {
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
                        .addOnFailureListener { error ->
                            trySend(
                                ResultState.Error(
                                    error.localizedMessage ?: "Something Went Wrong"
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

    override fun loginUserWithEmailAndPassword(
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
}

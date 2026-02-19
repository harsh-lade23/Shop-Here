package com.harsh.shophere.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.shophere.common.ORDER_COLLECTION
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.repository.OrderRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : OrderRepository {

    override fun placeOrder(ordersData: OrdersData): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        
        val userId = firebaseAuth.currentUser?.uid ?: run {
            trySend(ResultState.Error("User is not logged in"))
            close()
            return@callbackFlow
        }
        Log.d("Tracking place order", "placeOrder: trying to place order")


        firebaseFirestore.collection(ORDER_COLLECTION)
            .add(ordersData.copy(userId = userId))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Tracking place order", "placeOrder: uploaded")
                    trySend(ResultState.Success("Order Added Successfully"))
                } else {
                    Log.d("Tracking place order", "placeOrder: got error -> ${it.exception}")

                    trySend(

                        ResultState.Error(
                            it.exception?.localizedMessage ?: "Failed to add Order"
                        )
                    )
                }
            }
        awaitClose()
    }

    override fun getAllOrders(): Flow<ResultState<List<OrdersData>>> = callbackFlow {
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
}

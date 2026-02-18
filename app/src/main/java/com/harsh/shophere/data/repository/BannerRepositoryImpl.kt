package com.harsh.shophere.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.shophere.common.BANNER_COLLECTION_2
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.BannerDataModel
import com.harsh.shophere.domain.repository.BannerRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : BannerRepository {

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
}

package com.harsh.shophere.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.harsh.shophere.common.CATEGORIES_COLLECTION_2
import com.harsh.shophere.common.PRODUCT_COLLECTION_2
import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CategoryDataModel
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.domain.repository.CategoryRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : CategoryRepository {

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

    override fun getProductsByCategory(categoryId: String): Flow<ResultState<List<ProductsDataModel>>> =
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

    override fun searchCategories(query: String): Flow<ResultState<List<CategoryDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(CATEGORIES_COLLECTION_2)
                .orderBy("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
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
}

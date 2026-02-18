package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CategoryDataModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class GetAllCategoryUseCase @Inject constructor(private val repository: Repository) {

    fun getAllCategoryList(): Flow<ResultState<List<CategoryDataModel>>>{
        return repository.getAllCategories()
    }

}
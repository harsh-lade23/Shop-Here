package com.harsh.shophere.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CategoryDataModel
import com.harsh.shophere.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchCategoryByNameUseCase @Inject constructor(
    private val repository: Repository
) {
    fun searchCategoryByName(name: String): Flow<ResultState<List<CategoryDataModel>>>{
        return repository.searchCategoriesByName(name)
    }
}
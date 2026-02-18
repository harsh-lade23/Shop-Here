package com.harsh.shophere.features.category.domain.usecase

import com.harsh.shophere.common.ResultState
import com.harsh.shophere.domain.models.CategoryDataModel
import com.harsh.shophere.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(query: String): Flow<ResultState<List<CategoryDataModel>>> {
        return categoryRepository.searchCategories(query)
    }
}

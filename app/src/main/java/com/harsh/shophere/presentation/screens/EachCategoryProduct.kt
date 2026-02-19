package com.harsh.shophere.presentation.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.presentation.screens.homeScreen.ProductCard
import com.harsh.shophere.features.category.presentation.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EachCategoryProductScreenUi(
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    navController: NavController,
    categoryId: String,
    categoryName: String
) {
    val state = categoryViewModel.categoryProductState.collectAsStateWithLifecycle()
    val products = state.value.products
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var focusRequester= FocusRequester()
    var focusManager= LocalFocusManager.current

    LaunchedEffect(Unit) {
        categoryViewModel.loadProductsByCategory(categoryId)
    }

    var searchProductQuery = remember { mutableStateOf("") }
    var finalProductList = remember { mutableStateOf<List<ProductsDataModel?>>(emptyList()) }

    when {
        state.value.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.value.errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${state.value.errorMessage!!}")
            }
        }

        products.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No Products Available")
            }
        }

        else -> {
            finalProductList.value = products
            Scaffold(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                /*TODO- Update with category Name*/
                                text = categoryName,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        scrollBehavior = scrollBehavior

                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    OutlinedTextField(
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                /*todo-on search*/
                            }
                        ),
                        value = searchProductQuery.value,
                        onValueChange = { it ->
                            searchProductQuery.value = it
                            finalProductList.value = products.filter { product ->
                                product.name.contains(searchProductQuery.value, ignoreCase = true)
                            }

                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(R.color.darkBeige),
                            cursorColor = colorResource(R.color.darkBeige)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .focusRequester(focusRequester)
                            .clickable(
                                onClick = {
                                    focusRequester.requestFocus()
                                }

                            )
                            .focusable(),
                        placeholder = {
                            Text(text = "Search")
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            focusManager.clearFocus()
                                        }

                                    )
                            )
                        }
                    )
                    if (finalProductList.value.isEmpty()) {
                        Text(
                            "No product found",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(finalProductList.value) { product ->
                                product?.let {
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            Log.d("navigation", "EachCategoryScreen: success: ${product}")
                                            navController.navigate(
                                                Routes.EachProductDetailsScreen(
                                                    productId = product.productId
                                                )
                                            )
                                        }

                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }

}
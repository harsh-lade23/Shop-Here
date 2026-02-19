package com.harsh.shophere.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.harsh.shophere.R
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.presentation.screens.homeScreen.ProductCard
import com.harsh.shophere.features.product.presentation.ProductListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetAllProductScreen(
    productListViewModel: ProductListViewModel = hiltViewModel(),
    navController: NavController,
) {
    val productState = productListViewModel.productState.collectAsStateWithLifecycle()
    val products = productState.value.products
    val searchResults = productState.value.searchResults



    val searchQueryValue = remember {
        mutableStateOf(
            TextFieldValue("", selection = TextRange(0))
        )
    }

    LaunchedEffect(searchQueryValue.value.text) {
        productListViewModel.searchProducts(searchQueryValue.value.text.trim())
    }

    val  scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold (
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All Products",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior=scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ){innerPadding->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            OutlinedTextField(
                value = searchQueryValue.value,
                onValueChange = {/*TODO- Search Functionality*/
                    searchQueryValue.value=it.copy(selection = TextRange(it.text.length))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.darkBeige),
                    cursorColor = colorResource(R.color.darkBeige)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            )

            if (searchQueryValue.value.text.isEmpty()) {

                when {
                    productState.value.isLoading -> {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    productState.value.errorMessage != null -> {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error: ${productState.value.errorMessage!!}")
                        }
                    }

                    products.isEmpty() -> {
                        Log.d("search", "GetAllProductScreen: isempty")

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No Products Available")
                        }
                    }

                    else -> {
                        Log.d("search", "GetAllProductScreen: else:${products}")

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),

                            ) {
                            Log.d("search", "GetAllProductScreen: inside else: ${products}")

                            items(products) { product ->
                                Log.d("search", "GetAllProductScreen: checking product empty: ${product}")

                                product?.let {
                                    ProductCard(product) {
                                        Log.d("search", "GetAllProductScreen: success: ${product}")
                                        navController.navigate(
                                            Routes.EachProductDetailsScreen(
                                                productId = product.productId
                                            )
                                        )
                                    }
                                }

                            }
                        }
                    }


                }
            }
            else{
                when {
                    productState.value.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    productState.value.errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error: ${productState.value.errorMessage!!}")
                        }
                    }

                    searchResults.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No Products Available")
                        }
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),

                            ) {
                            /*TODO - Probably the search function is working wrong, check properly after adding more products*/
                            items(searchResults) { product ->
                                product?.let {
                                    ProductCard(product) {
                                        navController.navigate(
                                            Routes.EachProductDetailsScreen(
                                                productId = product.productId
                                            )
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

}













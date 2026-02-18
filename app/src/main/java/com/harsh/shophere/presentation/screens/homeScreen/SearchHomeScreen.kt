package com.harsh.shophere.presentation.screens.homeScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.features.search.presentation.SearchViewModel


@Composable
fun SearchHomeProductResultScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier
) {

    val searchQueryValue = remember {
        mutableStateOf(
            TextFieldValue("", selection = TextRange(0))
        )
    }

    val focusRequester = remember { FocusRequester() }
    val showFinalSearchData = remember { mutableStateOf(false) }

    /*TODO-Show Different Pages with limited products*/
    val searchState = searchViewModel.searchState.collectAsStateWithLifecycle()
    val searchResult = searchState.value.products

    val showSearchList = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val interactionSource = remember { MutableInteractionSource() }

    var searchStateForGrid = remember { mutableStateOf<List<ProductsDataModel?>>(emptyList()) }



    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LaunchedEffect(searchQueryValue.value.text) {
        if (searchQueryValue.value.text.isNotBlank()) {
            searchViewModel.searchProducts(searchQueryValue.value.text.trim())
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(0.dp, 40.dp, 0.dp, 23.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                keyboardOptions = KeyboardOptions.Default.copy(
                    showKeyboardOnFocus = true,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (!searchQueryValue.value.text.isEmpty()) {
                            showFinalSearchData.value = true
                        }
                        searchStateForGrid.value = searchResult
                        focusManager.clearFocus()
                    }
                ),
                value = searchQueryValue.value,
                onValueChange = {
                    searchQueryValue.value = it.copy(selection = TextRange(it.text.length))
                },
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        "search",
                        modifier = Modifier.clickable(
                            onClick = {
                                if (!searchQueryValue.value.text.isEmpty()) {
                                    showFinalSearchData.value = true
                                }
                                searchStateForGrid.value = searchResult
                                focusManager.clearFocus()
                            }
                        )
                    )/*TODO-show detail product card on click*/
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        Log.d(
                            "Focus Check",
                            "SearchHomeProductResultScreen: onfocuschange ->isFocused${it.isFocused}\nhasFocus${it.hasFocus}"
                        )
                        showSearchList.value = it.isFocused || it.hasFocus
                    }
                    .focusable(),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colorResource(R.color.white),
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorResource(R.color.darkBeige)
                )
            )


        }
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            if (showFinalSearchData.value) {
                /*
                                when {
                                    searchState.value.isLoading -> {

                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }

                                    searchState.value.errorMessage != null -> {

                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "Error: ${searchState.value.errorMessage!!}")
                                        }
                                    }

                                    searchResult.isEmpty() -> {
                                        Log.d("search", "GetAllProductScreen: isempty")

                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = "No Products Available")
                                        }
                                    }

                                    else -> {

                 */
                if (searchStateForGrid.value.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Products Found")
                    }
                } else {
                    Log.d("search", "GetAllProductScreen: else:${searchResult}")

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(10.dp),
                        modifier = Modifier.fillMaxSize()

                    ) {

                        items(searchStateForGrid.value)
                        { product ->
                            Log.d(
                                "search",
                                "GetAllProductScreen: checking product empty: ${product}"
                            )

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

            if (showSearchList.value) {
                if (searchQueryValue.value.text.isEmpty()) {
                    /*TODO - Implement Before search screen*/
                } else {

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.35f))
                            .clickable(
                                onClick = {
                                    focusManager.clearFocus()
                                },
                                interactionSource = interactionSource,
                                indication = null
                            )
                    )
                    when {
                        searchState.value.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .background(Color.White)
                                    .align(Alignment.Center),
                                color = colorResource(R.color.darkBeige)
                            )
                        }

                        searchState.value.errorMessage != null -> {
                            Text(
                                "Error: ${searchState.value.errorMessage}",
                                modifier = Modifier
                                    .background(Color.White)
                            )
                        }

                        searchState.value.products.isEmpty() -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .border(
                                        0.01.dp,
                                        shape = RectangleShape,
                                        color = Color(0xFFF1F1F1)
                                    )
                                    .background(Color.White)
                                    .clickable(
                                        onClick = {},
                                        interactionSource = interactionSource,
                                        indication = null
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {


                                Text(
                                    text = "No Product Found!",
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                )

                            }
                        }

                        else -> {

                            LazyColumn(Modifier.fillMaxSize()
                                .padding( bottom = 8.dp)
                            ) {
                                /*TODO-Define custom height*/
                                items(searchResult) { product ->
                                    product?.let {
                                        SearchResultCard(product.name) {
                                            navController.navigate(
                                                Routes.EachProductDetailsScreen(
                                                    product.productId
                                                )
                                            )
                                        } /*TODO - Show custom product card instead of text*/
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

@Composable
private fun SearchResultCard(
    productName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.01.dp, shape = RectangleShape, color = Color(0xFFF1F1F1))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search Icon
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.Gray,
            modifier = Modifier
                .size(20.dp)
                .weight(.1f)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = productName,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(.75f)
        )




        Spacer(modifier = Modifier.width(12.dp))

        // Filter/Sort Icon
        IconButton(
            onClick = {/*TODO*/ },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Done, // or Icons.Default.FilterList
                contentDescription = "Filter",
                tint = Color.Gray,
                modifier = Modifier
                    .size(18.dp)
                    .weight(.15f)
            )
        }
    }
}
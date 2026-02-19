package com.harsh.shophere.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.features.wishlist.presentation.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetAllFavScreenUi(
    wishlistViewModel: WishlistViewModel = hiltViewModel(),
    navController: NavController,
) {

    /*TODO- show products in different pages and load one by one as user request*/
    val wishlistState = wishlistViewModel.wishlistState.collectAsStateWithLifecycle()
    val wishlistData = wishlistState.value.wishlistItems

    var searchWishlistItemQuery = remember{ mutableStateOf("") }
    var searchedWishlist = remember { mutableStateOf<List<WishListItemModel>>(emptyList()) }

    var focusRequester = FocusRequester()
    var focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        wishlistViewModel.loadWishlist()
    }

    // Local filtering for search
    LaunchedEffect(searchWishlistItemQuery.value, wishlistData) {
        searchedWishlist.value = if (searchWishlistItemQuery.value.isEmpty()) {
            wishlistData
        } else {
            wishlistData.filter { item ->
                item.name.contains(searchWishlistItemQuery.value, ignoreCase = true)
            }
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wish List",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ){ innerPadding->
        Column(Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = 10.dp)
            .padding(innerPadding)
        ) {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                    /*todo-on search*/
                        focusManager.clearFocus()
                    }
                ),
                value = searchWishlistItemQuery.value,
                onValueChange = { it ->
                    searchWishlistItemQuery.value=it
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.darkBeige),
                    cursorColor = colorResource(R.color.darkBeige)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(focusRequester)
                    .focusable()
                    .clickable(onClick = {focusRequester.requestFocus() }),
                placeholder = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .clickable(onClick = {focusManager.clearFocus() }),
                    )
                }
            )

            when{
                wishlistState.value.isLoading->{
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }

                }
                wishlistState.value.errorMessage!=null->{
                    Box(modifier= Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "Error: ${wishlistState.value.errorMessage!!}")
                    }
                }
                wishlistData.isEmpty()->{
                    Box(modifier= Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "Your Wishlist is empty.")
                    }
                }
                searchedWishlist.value.isEmpty()->{
                    Box(modifier= Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "No Product Found.")
                    }
                }
                else->{

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)

                    ) {
                        items (searchedWishlist.value) {wishListItem->
                            WishListCard(
                                wishListItem = wishListItem,
                                onProductClick = {
                                    navController.navigate(Routes.EachProductDetailsScreen(productId = wishListItem.productId))
                                },
                                onRemoveClick = {
                                    wishlistViewModel.removeFromWishlist(wishListItem.wishListItemId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WishListCard(
    wishListItem: WishListItemModel,
    onProductClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        onClick = onProductClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Column {
                AsyncImage(
                    model = wishListItem.image,
                    contentDescription = wishListItem.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = wishListItem.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = wishListItem.price,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Remove button
            androidx.compose.material3.IconButton(
                onClick = onRemoveClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove from wishlist",
                    tint = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier
                        .shadow(4.dp, spotColor = colorResource(R.color.creamy) )

                )
            }
        }
    }
}
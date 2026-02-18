package com.harsh.shophere.presentation.screens.homeScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.ProductsDataModel
import com.harsh.shophere.presentation.Utils.ImageBanner
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.features.home.presentation.HomeViewModel
import com.harsh.shophere.features.user.presentation.UserViewModel

@Composable
fun HomeScreenUi(
    homeViewModel: HomeViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    navController: NavController,
    firebaseAuth: FirebaseAuth
) {

    val homeState = homeViewModel.homeState.collectAsStateWithLifecycle()
    val userState = userViewModel.userState.collectAsStateWithLifecycle()
    val isSearching = remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        userViewModel.loadUser(firebaseAuth.currentUser!!.uid)
        homeViewModel.loadSuggestedProducts()
    }

    BackHandler {
        if(isSearching.value){
            isSearching.value=false
        }
    }

    if (homeState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    else if (homeState.value.errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = homeState.value.errorMessage!!,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    else {

        Scaffold(

        )
        { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(bottom = 14.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                if (isSearching.value) {
                    SearchHomeProductResultScreen(navController = navController, modifier = Modifier.weight(1f))
                }
                else {
                    /* Welcome Row */

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp, 40.dp, 14.dp, 23.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                        ) {

                            if (userState.value.user != null){
                                Text(
                                    text = "Welcome Back",
                                    fontSize = 18.sp,
                                )
                                Text(
                                    text = userState.value.user!!.firstName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                            else if (userState.value.isLoading){
                                Text(
                                    text = "Loading...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                            else {
                                Text(
                                    text = "Welcome Back",
                                    fontSize = 22.sp,
                                )
                            }

                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            IconButton(
                                onClick = {isSearching.value=true },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = colorResource(R.color.icon_background)
                                )

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search icon"
                                )
                            }
                            IconButton(
                                onClick = {/*TODO- Implement Notifications*/ },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = colorResource(R.color.icon_background)
                                )

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notification icon"
                                )
                            }
                        }

                    }

                    homeState.value.banners?.let { banner ->
                        ImageBanner(banners = banner)

                    }



                    //Category Section

                    Column(
                        modifier = Modifier.padding(top = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                "Categories",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Text(
                                "See All", color = colorResource(R.color.darkBeige),
                                modifier = Modifier.clickable {
                                    navController.navigate(Routes.AllCategoriesScreen)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )

                        }
                        LazyRow(
                            modifier = Modifier
                                .padding(start = 3.dp)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues()
                        ) {
                            items(homeState.value.categories ?: emptyList()) { category ->
                                CategoryItem(
                                    imageUri = category.categoryImage,
                                    category = category.name,
                                    onClick = {
                                        navController.navigate(
                                            Routes.EachCategoryItemsScreen(
                                                category.categoryId,
                                                categoryName = category.name
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }


                    //Most Popular Section
                    Column(
                        modifier = Modifier.padding(top = 18.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Featured Products",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                "See More",
                                color = colorResource(R.color.darkBeige),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.clickable {
                                    navController.navigate(Routes.SeeAllProducts)

                                }
                            )

                        }


                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(homeState.value.products ?: emptyList()) { product ->
                                ProductCard(
                                    product = product,
                                ){
                                    navController.navigate(Routes.EachProductDetailsScreen(product.productId))

                                }
                            }
                        }
                    }


                    // Build the suggested for you
                    Column(
                        modifier = Modifier.padding(top = 18.dp, bottom = 7.dp)
                    ) {
                        when {
                            homeState.value.suggestedProducts == null -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }

                            homeState.value.suggestedProductsError != null -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(text = homeState.value.suggestedProductsError!!)
                                }
                            }

                            homeState.value.suggestedProducts?.isEmpty() == true -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(text = "No Product to suggest Like One")
                                }
                            }

                            else -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Quick Access",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "See More",
                                        color = colorResource(R.color.darkBeige),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.clickable {
                                            navController.navigate(Routes.SeeAllProducts)

                                        }
                                    )


                                }


                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(homeState.value.suggestedProducts ?: emptyList()) { product ->
                                        ProductCard(
                                            product = product,
                                        ){
                                            navController.navigate(Routes.EachProductDetailsScreen(product.productId))
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


}



@Composable
fun CategoryItem(
    imageUri: String,
    category: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 11.dp)
            .width(70.dp)
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .background(
                    colorResource(R.color.lightGray),
                    shape = CircleShape
                )
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
            )
        }
        Text(
            category,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 2.dp),
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontSize = 11.sp
        )

    }
}


// Flesh sell section

@Composable
fun ProductCard(
    product: ProductsDataModel,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(175.dp)
            .clickable(onClick=onClick),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = product.image[0],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio(0.9f),
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = product.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "₹${product.finalPrice}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    if (product.availableUnits < 20) {
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${product.availableUnits} left",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorResource(R.color.gray)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "₹${product.price}",
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.LineThrough,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = colorResource(R.color.gray),
                    )


                }
            }
        }
    }

}












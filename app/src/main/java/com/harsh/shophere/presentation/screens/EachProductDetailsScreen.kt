package com.harsh.shophere.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.CartItemModel
import com.harsh.shophere.domain.models.OrderItem
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.models.WishListItemModel
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.presentation.viewModels.ShopViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EachProductDetailsScreen(
    shopViewModel: ShopViewModel=hiltViewModel(),
    navController: NavController,
    productId: String
) {
    val getProductById=shopViewModel.getProductByIdState.collectAsStateWithLifecycle()

    val context=LocalContext.current
    var selectedSize by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }
    var isFavorite by remember { mutableStateOf(false) }

    val scrollState=rememberScrollState()

    Log.d("Firebase", "EachProductDetailsScreen: ")
    LaunchedEffect(Unit) {
        Log.d("Firebase", "EachProductDetailsScreen: calling function")
        shopViewModel.getProductById(productId)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBarsIgnoringVisibility)


        /*
                topBar = {

                    TopAppBar(
                        title = {Text("Product Details") },
                        navigationIcon = {
                            IconButton(onClick = {navController.popBackStack()}) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Icon")
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                    }

         */

    ) {innerPadding->

        when{
            getProductById.value.isLoading->{
                Box(modifier= Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }

            getProductById.value.errorMessage!=null || getProductById.value.variantErrorMessage!=null->
                Text(text = getProductById.value.errorMessage!! + " "+ getProductById.value.variantErrorMessage)

            getProductById.value.productData!=null && getProductById.value.variantDataList!=null->{
                val productData=getProductById.value.productData!!.copy(productId=productId)
                val variantDataList=getProductById.value.variantDataList!!

                val currentVariant= remember { mutableIntStateOf(0) }

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(bottom = 14.dp)
                        .verticalScroll(scrollState)
                ){
                    AnimatedContent(
                        targetState = currentVariant.intValue,
                        label="VariantAnimationChange",
                        transitionSpec = {
                            slideInHorizontally { fullWidth -> fullWidth } togetherWith
                                    slideOutHorizontally { fullWidth -> -fullWidth }
                        }
                    ) { targetVariantIndex ->
                        val variant = variantDataList[targetVariantIndex]
                        Column {
                            Box(modifier = Modifier.height(410.dp)) {


                                ImageSlider(variant.imageList)
                            }

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = variant.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Spacer(modifier = Modifier.padding(10.dp))
                                Text(
                                    text = "Product Details",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Spacer(modifier = Modifier.padding(5.dp))
                                Text(
                                    text = variant.description,
                                    fontSize = 13.sp,
                                    lineHeight = 13.sp,
                                    color = Color(0xFF525252),
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }



                            if (variantDataList.size > 1) {
                                Spacer(modifier = Modifier.padding(5.dp))
                                HorizontalDivider(
                                    Modifier
                                        .padding(horizontal = 8.dp)
                                        .fillMaxWidth(),
                                )
                                Spacer(modifier = Modifier.padding(8.dp))

                                Text(
                                    text = "Color",
                                    modifier = Modifier.padding(start = 16.dp),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(Modifier.padding(3.dp))

                                LazyRow(
                                    modifier = Modifier.padding(start = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    items(variantDataList.size) { variantIndex ->
                                        var variantItem = variantDataList[variantIndex]
                                        Column(
                                            modifier = Modifier.clickable(
                                                onClick = {
                                                    currentVariant.intValue = variantIndex
                                                }
                                            ),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            AsyncImage(
                                                model = variantItem.imageList[0],
                                                contentDescription = "Variant Image",
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .size(55.dp),
                                                contentScale = ContentScale.Crop,
                                            )
                                            Text(
                                                text = variantItem.color,
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        }
                                    }

                                }
                            }


                            Spacer(modifier = Modifier.padding(5.dp))
                            HorizontalDivider(
                                Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth(),
                            )



                            Spacer(modifier = Modifier.padding(8.dp))



                            Text(
                                text = "Available Size",
                                modifier = Modifier.padding(start = 16.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.padding(3.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(start = 14.dp)
                            ) {
                                variant.size.forEach { size ->
                                    var containerColor =
                                        if (selectedSize == size) colorResource(R.color.darkBeige)
                                        else Color.Transparent
                                    var contentColor =
                                        if (selectedSize == size) colorResource(R.color.white)
                                        else colorResource(R.color.darkBeige)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(5.dp))
                                            .background(containerColor)
                                            .border(
                                                0.1.dp,
                                                shape = RoundedCornerShape(5.dp),
                                                color = colorResource(R.color.lightGray)
                                            )
                                            .clickable(
                                                onClick = { selectedSize = size }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {


                                        Text(
                                            text = size,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontSize = 12.sp,
                                            color = contentColor,
                                            modifier = Modifier
                                                .padding(vertical = 10.dp, horizontal = 20.dp)
                                        )

                                    }

                                }
                            }
                            Spacer(modifier = Modifier.padding(12.dp))

                            Text(
                                text = buildAnnotatedString {
                                    append("Select Color: ")
                                    pushStyle(SpanStyle(color = Color.Gray))
                                    append(variant.color)
                                },
                                modifier = Modifier.padding(start = 16.dp),
                                style = MaterialTheme.typography.titleSmall
                            )




                            Spacer(modifier = Modifier.padding(24.dp))




                            Text(
                                text = "Quantity",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                            )
                            Row(
                                modifier = Modifier.padding(start = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (quantity > 1) quantity--
                                    },

                                    ) {
                                    Text(
                                        "-", style = MaterialTheme.typography.headlineSmall,
                                        color = colorResource(R.color.white),
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(colorResource(R.color.darkBeige))
                                            .padding(horizontal = 15.dp)
                                    )
                                }
                                AnimatedContent(
                                    targetState = quantity,
                                    label = "QuantityAnimation",
                                    transitionSpec = {
                                        if (targetState > initialState) {
                                            slideInVertically { height -> height } togetherWith
                                                    slideOutVertically { height -> -height }
                                        } else {
                                            slideInVertically { height -> -height } togetherWith
                                                    slideOutVertically { height -> height }
                                        }
                                    }
                                ) { targetQuantity ->
                                    Text(
                                        targetQuantity.toString(),
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                }
                                IconButton(
                                    onClick = {
                                        if (quantity < 10) quantity++
                                    }
                                ) {
                                    Text(
                                        "+",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = colorResource(R.color.white),
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(colorResource(R.color.darkBeige))
                                            .padding(horizontal = 10.dp)
                                    )
                                }
                            }

                        }
                    }





                    Button(
                        onClick = {
                            val selectedOption= mutableMapOf<String,String>()
                            if(selectedSize.isEmpty()){
                                Toast.makeText(context, "Please select size", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                selectedOption[selectedSize] =
                                    "color"/*TODO-Update the correct color*/
                                val cartDataModel = CartItemModel(
                                    name = variantDataList[currentVariant.intValue].name,
                                    image = variantDataList[currentVariant.intValue].imageList[0],
                                    price = variantDataList[currentVariant.intValue].price,
                                    quantity = quantity,
                                    productId = productData.productId,
                                    selectedOptions = selectedOption,
                                    variantId = variantDataList[currentVariant.intValue].variantId

                                )
                                shopViewModel.addToCart(cartDataModel)
                            }
                                  },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        colors=ButtonDefaults.buttonColors(colorResource(R.color.darkBeige))
                    ) {
                        Text(text = "Add to Cart")
                    }


                        Button(
                        onClick = {
                            if(selectedSize.isEmpty()){
                                Toast.makeText(context, "Please select size", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                val ordersData= OrdersData(
                                    totalPrice=((productData.price.toInt())* quantity).toString(),
                                    orderItems = listOf(OrderItem(
                                        image=variantDataList[currentVariant.intValue].imageList[0],
                                        name = variantDataList[currentVariant.intValue].name,
                                        productId=productData.productId,
                                        quantity=quantity,
                                        price = variantDataList[currentVariant.intValue].finalPrice,
                                        variantId = variantDataList[currentVariant.intValue].variantId,
                                        selectedOptions = mapOf(variantDataList[currentVariant.intValue].color to selectedSize)
                                    )
                                    )
                                )
                                navController.currentBackStackEntry?.savedStateHandle?.set("orderData", ordersData)

                                navController.navigate(Routes.CheckOutScreen)
                            }
                        },

                        modifier= Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        colors=ButtonDefaults.buttonColors(colorResource(R.color.darkBeige))
                    ) {
                        Text(text = "Buy Now")
                    }

                    OutlinedButton(
                        onClick = {

                            isFavorite = !isFavorite
                            val wishListItemModel= WishListItemModel(
                                productId=productData.productId,
                                name = variantDataList[currentVariant.intValue].name,
                                image = variantDataList[currentVariant.intValue].imageList[0],
                                price = variantDataList[currentVariant.intValue].finalPrice
                            )
                            shopViewModel.addToFav(wishListItemModel)
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        val scale = remember { Animatable(1f) }

                        LaunchedEffect(isFavorite) {
                            if (isFavorite) {
                                launch {
                                    scale.animateTo(
                                        targetValue = 1.3f,
                                        animationSpec = tween(durationMillis = 100)
                                    )
                                    scale.animateTo(
                                        targetValue = 1f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                            }
                        }


                        Row {
                            Icon(
                                imageVector =
                                    if (isFavorite)Icons.Default.Favorite
                                    else Icons.Default.FavoriteBorder,
                                tint = colorResource(R.color.darkBeige),
                                contentDescription = "Favorite Icon",
                                modifier = Modifier.scale(scale.value)
                            )
                            Text(
                                text = "Add to Wishlist",
                                modifier = Modifier.padding(start = 8.dp),
                                color=colorResource(R.color.darkBeige)
                                )
                        }
                    }




                }

            }
        }







    }

}


@Composable
fun ImageSlider(
    imageList: List<String>,
){
    val pagerState=rememberPagerState(pageCount = {imageList.size})
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true){
            delay(3000)
            val nextPage = (pagerState.currentPage+1) % imageList.size

            scope.launch {
                pagerState.animateScrollToPage(nextPage)
            }


        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .wrapContentSize()
        ){
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .wrapContentSize()
            ) { currentPage->

                Card(
                    modifier = Modifier
                        .fillMaxSize(),
                    elevation = CardDefaults.elevatedCardElevation(8.dp)
                ) {
                    AsyncImage(model=imageList[currentPage],
                        contentDescription = "$currentPage",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                }

            }
        }


    }
}












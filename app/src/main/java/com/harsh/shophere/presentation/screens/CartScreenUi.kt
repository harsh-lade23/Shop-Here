package com.harsh.shophere.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.features.cart.presentation.CartViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    navController: NavController,
) {
    val cartState = cartViewModel.cartState.collectAsStateWithLifecycle()
    val cartData = cartState.value.cartItems
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        cartViewModel.loadCart()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Cart",
                        modifier= Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(innerPadding)
                .padding(bottom = 42.dp)
                .fillMaxSize()
        ) {
            when {
                cartState.value.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                cartState.value.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Error: ${cartState.value.errorMessage!!}")
                    }
                }

                cartData.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Items in Cart")

                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {


                        LazyColumn(
                            modifier = Modifier
                                .padding(top = 10.dp)
                        ) {

                            items(cartData) { cartItem ->
                                CardItemCard(
                                    item = cartItem,
                                    isQuantityLoading = cartState.value.isLoading,
                                    onQuantityDecrement = {
                                        cartViewModel.decrementQuantity(cartItem.cartItemId, cartItem.quantity)
                                    },
                                    onQuantityIncrement = {
                                        cartViewModel.incrementQuantity(cartItem.cartItemId, cartItem.quantity)
                                    },
                                    onClick = {
                                        navController.navigate(Routes.EachProductDetailsScreen(cartItem.productId))
                                    }
                                )
                            }

                            item {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical =25.dp)
                                ) {
                                    Row (Modifier.padding()
                                        .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text("Item Total: ",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                            )
                                        Text("₹${cartState.value.totalPrice}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold)
                                    }

                                    Row (Modifier.padding(top = 1.dp)
                                        .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text("Tax: ",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold)
                                        Text("₹0.00",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold)
                                    }

                                    Row (Modifier.padding(top = 7.dp)
                                        .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text("Total: ",
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Bold)
                                        Text("₹${cartState.value.totalPrice}",
                                            fontSize = 19.sp,fontWeight = FontWeight.Bold)
                                    }
                                }

                            }
                            item {
                                Column {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                    )
                                    Button(
                                        onClick = {
                                            /*TODO-Handle the Checkout*/
                                            if (cartData.isEmpty()) {
                                                Toast.makeText(
                                                    context,
                                                    "Add Products before checkout",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                var totalPrice = 0;
                                                var orderItemList = mutableListOf<OrderItem>()
                                                cartData.forEach { cartItem ->
                                                    totalPrice += (cartItem?.quantity
                                                        ?: 1) * (cartItem?.price?.toInt()
                                                        ?: 1)
                                                    orderItemList.add(
                                                        OrderItem(
                                                            image = cartItem!!.image,
                                                            name = cartItem.name,
                                                            productId = cartItem.productId,
                                                            quantity = cartItem.quantity,
                                                            price = cartItem.price,
                                                            variantId = cartItem.variantId,
                                                            selectedOptions = cartItem.selectedOptions
                                                        )
                                                    )
                                                }
                                                val ordersData = OrdersData(
                                                    totalPrice = totalPrice.toString(),
                                                    orderItems = orderItemList
                                                )


                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "orderData",
                                                    ordersData
                                                )
                                                navController.navigate(Routes.CheckOutScreen)

                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(colorResource(R.color.darkBeige))
                                    ) {
                                        Text(
                                            text = "Checkout",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 4.dp)
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


@Composable
fun CardItemCard(
    item: CartItemModel,
    isQuantityLoading: Boolean,
    onClick:()->Unit,
    onQuantityIncrement: (String) -> Unit,
    onQuantityDecrement: (String) -> Unit,
) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(115.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Size: ${item.selectedOptions.keys.first()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "₹${item.price.toFloat()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = "Price: ₹${item.price.toFloat() * item.quantity}",
                    modifier = Modifier.padding(top=2.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 16.dp, bottom = 30.dp)
                    .align(Alignment.Bottom)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(colorResource(R.color.lightBeige))
                ) {
                    IconButton(
                        onClick = { if (item.quantity > 1) onQuantityDecrement(item.cartItemId) },
                        modifier = Modifier
                            .background(colorResource(R.color.white), shape = CircleShape)
                            .size(26.dp)
                    ) {
                        Text("-", fontWeight = FontWeight.Bold)
                    }

                    if (isQuantityLoading) {
                        CircularProgressIndicator(modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(14.dp),
                            strokeWidth = 2.dp,
                            color = colorResource(R.color.mediumBeige)
                        )
                    } else {
                        Text(
                            text = item.quantity.toString(),
                            modifier = Modifier.padding(horizontal = 9.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    IconButton(
                        onClick = { if (item.quantity < 10) onQuantityIncrement(item.cartItemId) },
                        modifier = Modifier
                            .background(colorResource(R.color.darkBeige), shape = CircleShape)
                            .size(26.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            modifier = Modifier.size(17.dp),
                            contentDescription = "Increase",
                            tint = colorResource(R.color.white)
                        )
                    }
                }

            }
        }

    }

}
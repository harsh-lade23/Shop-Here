package com.harsh.shophere.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.OrderItem
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.features.order.presentation.OrderViewModel
import com.harsh.shophere.features.user.presentation.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(
    orderViewModel: OrderViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    navController: NavController,
    ordersData: OrdersData
) {

    val orderState = orderViewModel.orderState.collectAsStateWithLifecycle()
    val userState = userViewModel.userState.collectAsStateWithLifecycle()
    val context= LocalContext.current
    val selectedMethod = remember { mutableStateOf("")  }

    val ordersDataState =remember { mutableStateOf(ordersData) }
    val savedStateHandle=navController.currentBackStackEntry?.savedStateHandle

    val selectedShippingDetails=remember { mutableStateOf<ShippingDetails?>(null) }

    LaunchedEffect(Unit) {
        savedStateHandle?.getLiveData<OrdersData>("orderData")?.observeForever {
                updated->
            if(updated!=null) {
                ordersDataState.value = updated
                selectedShippingDetails.value = updated.shippingDetails
            }
        }
        userViewModel.loadShippingAddresses()
    }

    LaunchedEffect(orderState.value.isOrderPlaced) {
        if (orderState.value.isOrderPlaced) {
            Toast.makeText(
                context,
                "Your order has been confirmed",
                Toast.LENGTH_SHORT
            ).show()
            navController.currentBackStackEntry?.savedStateHandle?.set("orderData", ordersDataState.value)
            navController.navigate(Routes.OrderConfirmationScreen)
            orderViewModel.resetOrderPlacedState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Shipping") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Icon")
                    }
                }
            )
        }
    ) {
        innerPadding->

            LazyColumn(modifier= Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
            ) {

                item{
                    Text(text = "Products",
                        modifier=Modifier.padding(top=20.dp, bottom=12.dp),
                        style=MaterialTheme.typography.titleMedium

                    )
                }
                items (ordersDataState.value.orderItems){
                    ProductDetail(it)
                }


                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    when{
                        userState.value.isLoading->{
                            LinearProgressIndicator(color=colorResource(R.color.darkBeige))
                        }
                        userState.value.errorMessage!=null || userState.value.shippingAddresses.isEmpty() ->{
                            Column {
                                Text(text = "No shipping addresses found",
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set("orderData", ordersDataState.value)
                                        navController.navigate(Routes.ShippingInformationScreen)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(5.dp),
                                    border = BorderStroke(1.dp, colorResource(R.color.darkBeige)),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = colorResource(R.color.darkBeige),
                                    )
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add", tint = colorResource(R.color.darkBeige))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add Shipping Address")
                                }
                            }
                        }
                        userState.value.shippingAddresses.isNotEmpty()->{
                          var expanded by remember { mutableStateOf(false) }

                            val shippingList = userState.value.shippingAddresses

                            Column {
                                Text("Select Shipping Address", style = MaterialTheme.typography.titleMedium)

                                OutlinedButton(
                                    onClick = { expanded = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, colorResource(R.color.darkBeige)),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = colorResource(R.color.darkBeige),
                                        containerColor = colorResource(R.color.medium_light_beige)
                                    )
                                ) {
                                    Column {
                                        var text=if(selectedShippingDetails.value==null)"Choose Shipping Info"
                                        else "${selectedShippingDetails.value!!.name}\n${selectedShippingDetails.value!!.contactNo}\n${selectedShippingDetails.value!!.address.street}, ${selectedShippingDetails.value!!.address.village}, ${selectedShippingDetails.value!!.address.city}"

                                        Text(
                                            text = text,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .background(colorResource(R.color.medium_light_beige))
                                        .fillMaxWidth()
                                ) {
                                    if(shippingList.isEmpty()){
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = "Add Shipping Details",
                                                    maxLines = 3,
                                                    overflow = TextOverflow.Ellipsis,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier
                                                )
                                            },
                                            onClick = {
                                                navController.currentBackStackEntry?.savedStateHandle?.set("orderData", ordersDataState.value)
                                                navController.navigate(Routes.ShippingInformationScreen)
                                            },
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                                .border(
                                                    1.dp,
                                                    color = colorResource(R.color.darkBeige),
                                                    shape = RoundedCornerShape(8.dp)
                                                ),
                                            leadingIcon =  {Icon(Icons.Default.Add, contentDescription = "Add", tint = colorResource(R.color.darkBeige))}
                                        )
                                    }
                                    else {
                                        shippingList.forEach { shippingDetail ->
                                            Log.d("Dropdown", "CheckOutScreen: ${shippingDetail}")


                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "${shippingDetail.name}\n${shippingDetail.contactNo}\n${shippingDetail.address.street}, ${shippingDetail.address.village}, ${shippingDetail.address.city}",
                                                        maxLines = 3,
                                                        overflow = TextOverflow.Ellipsis,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        modifier = Modifier
                                                    )
                                                },
                                                onClick = {
                                                    selectedShippingDetails.value = shippingDetail
                                                    ordersDataState.value =
                                                        ordersDataState.value.copy(shippingDetails = shippingDetail)
                                                    expanded = false
                                                },
                                                modifier = Modifier
                                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                                    .border(
                                                        1.dp,
                                                        color = colorResource(R.color.darkBeige),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                            )

                                        }
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set("orderData", ordersDataState.value)
                                        navController.navigate(Routes.ShippingInformationScreen)
                                    },
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(5.dp),
                                    border = BorderStroke(1.dp, colorResource(R.color.darkBeige)),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = colorResource(R.color.darkBeige),
                                    )
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add", tint = colorResource(R.color.darkBeige))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add New Shipping Info")
                                }
                            }

                        }
                    }




                    Spacer(modifier= Modifier.height(30.dp))

                    Column {
                        Text(text = "Shipping Method",
                            style=MaterialTheme.typography.titleMedium)
                        Spacer(modifier= Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedMethod.value=="Cash On Deliver",
                                onClick = {
                                    selectedMethod.value="Cash On Deliver"
                                },
                                colors= RadioButtonDefaults.colors(
                                    selectedColor = colorResource(R.color.darkBeige) ,
                                    unselectedColor = colorResource(R.color.darkBeige)
                                )
                            )
                            Spacer(modifier=Modifier.width(8.dp))
                            Text("Cash On Deliver - Rs.50",
                                modifier = Modifier.clickable(onClick = {
                                    selectedMethod.value="Cash On Deliver"
                                })
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding( vertical =25.dp)
                    ) {
                        Text("Order Summary: ",
                            style= MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row (Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text("Item Total: ",
                                style=MaterialTheme.typography.bodyMedium,
                            )
                            Text("₹${ordersData.totalPrice}",
                                style=MaterialTheme.typography.bodyMedium,
                            )
                        }

                        Row (Modifier
                            .padding(horizontal = 16.dp, vertical = 1.dp)
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text("Delivery: ",
                                style=MaterialTheme.typography.bodyMedium,
                            )
                            Text("₹50",
                                style=MaterialTheme.typography.bodyMedium,
                            )
                        }

                        Row (Modifier
                            .padding(start = 16.dp, top = 10.dp, end = 16.dp)
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text("Total: ",
                                style=MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text("₹${ordersData.totalPrice.toInt()+50}",
                                style=MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold


                            )
                        }
                    }

                    Button(
                        onClick={/*TODO- Later -pay.invoke()*/
                            if (selectedMethod.value.isNotEmpty() && selectedShippingDetails.value!=null) {
                                orderViewModel.placeOrder(
                                    ordersDataState.value.copy(
                                        shippingMethods = selectedMethod.value
                                    ),
                                    navController
                                )
                                /*TODO- Later - After confirming the order.-> show animation ->clear cart-> show order summary*/
                            }
                            else if (selectedMethod.value.isEmpty()){
                                Toast.makeText(context, "Please choose shipping method", Toast.LENGTH_SHORT).show()
                            }
                            else if (selectedShippingDetails.value==null){
                                Toast.makeText(context, "Please choose shipping Address", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier= Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.darkBeige)),
                        enabled = !orderState.value.isLoading
                    ) {
                        if (orderState.value.isLoading) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = colorResource(R.color.white)
                            )
                        } else {
                            Text(text = "Continue to Shipping")
                        }
                    }

                }

            }

    }

}


@Composable
private fun ProductDetail(orderItem: OrderItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        AsyncImage(
            model = orderItem.image,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .border(1.dp, colorResource(R.color.gray)),

        )

        Column {
            Text(
                text = orderItem.name,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "₹${orderItem.price}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "QTY: ${orderItem.quantity}",
            style = MaterialTheme.typography.titleSmall,
        )
    }
}
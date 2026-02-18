package com.harsh.shophere.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.presentation.navigation.Routes
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun OrderConfirmationScreenUI(
    navController: NavController,
    ordersData: OrdersData
) {
    Scaffold(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxSize(),

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Order Confirmation",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.Center)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier
                            .padding(top = 21.dp, start = 10.dp)
                            .size(28.dp)
                            .clickable(
                                onClick = {
                                    navController.navigate(Routes.HomeScreen) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true

                                        restoreState = true     }

                                }
                            )
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Thank you for your order!",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.padding(5.dp))



                    Text(
                        text = "Your order has been placed and is being " +
                                "processed. You will receive an email " +
                                "confirmation shortly.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.padding(8.dp))

                    Box {
                        Log.d("Order Confirmation", "OrderConfirmationScreenUI: ${ordersData}")
                        Log.d("Order Confirmation", "OrderConfirmationScreenUI: order items ${ordersData.orderItems}")
                        AsyncImage(
                            model = ordersData.orderItems[0].image,
                            contentDescription = "Order Image",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .matchParentSize()
                                .padding(horizontal = 10.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.background.copy(
                                                alpha = 0.22f
                                            ),
                                            Color(0xCC1E1E1E)
                                        ),

                                        )
                                )
                        )
                        {
                            Row(
                                Modifier
                                    .padding(16.dp)
                                    .fillMaxHeight(),
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(5f)
                                ) {
                                    Text(
                                        text = ordersData.orderItems[0].name,
                                        style = MaterialTheme.typography.displayMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .align(Alignment.Start),
                                        color = colorResource(R.color.white)
                                    )
                                    Text(
                                        text = "Placed on ${LocalDate.now().plusDays(8).dayOfMonth} ${LocalDate.now().plusDays(8).month.name.lowercase().capitalize()} ${LocalDate.now().plusDays(8).year}",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .align(Alignment.Start),
                                        color = colorResource(R.color.lightGray)
                                    )
                                }

//Add when the order tracking feature is implemented
/*
                                Box(
                                    modifier= Modifier
                                        .align(Alignment.Bottom)
                                        .weight(3f)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(colorResource(R.color.mediumBeige))
                                        .clickable(
                                            onClick = {/*TODO*/ }
                                        ),
                                    contentAlignment = Alignment.Center

                                ) {
                                    Text(text = "View Details",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleSmall,
                                        color=colorResource(R.color.white),
                                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 2.dp)
                                    )
                                }

 */
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(30.dp))
                    Box(
                        modifier= Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                onClick = {
                                    navController.navigate(Routes.HomeScreen){
                                        popUpTo (navController.graph.findStartDestination().id){  }
                                        launchSingleTop=true
                                    }

                                }
                            )
                            .background(colorResource(R.color.darkBeige))
                            ,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Continue Shopping",
                            modifier= Modifier
                                .padding(vertical = 8.dp),
                            color=colorResource(R.color.white),
                            style=MaterialTheme.typography.titleMedium
                        )
                    }






            }
        }


    }











}
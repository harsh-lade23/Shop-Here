package com.harsh.shophere.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.google.firebase.auth.FirebaseAuth
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.presentation.screens.ShippingInformationScreenUI
import com.harsh.shophere.presentation.SignInScreen
import com.harsh.shophere.presentation.SignUpScreen
import com.harsh.shophere.presentation.screens.AllCategoriesScreen
import com.harsh.shophere.presentation.screens.CartScreen
import com.harsh.shophere.presentation.screens.CheckOutScreen
import com.harsh.shophere.presentation.screens.EachCategoryProductScreenUi
import com.harsh.shophere.presentation.screens.EachProductDetailsScreen
import com.harsh.shophere.presentation.screens.GetAllFavScreenUi
import com.harsh.shophere.presentation.screens.GetAllProductScreen
import com.harsh.shophere.presentation.screens.OrderConfirmationScreenUI
import com.harsh.shophere.presentation.screens.homeScreen.HomeScreenUi
import com.harsh.shophere.presentation.screens.ProfileScreenUi
import com.harsh.shophere.presentation.screens.userAccountScreen.UserAccountScreenUI


data class BottomNavItem(
    val name: String,
    val icon: ImageVector,
    val unselectedIcon: ImageVector,
)



@Composable
fun App(
    firebaseAuth: FirebaseAuth
){
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination=navBackStackEntry?.destination?.route

    val shouldDhowBottomBar = remember{mutableStateOf(false)}

    LaunchedEffect(currentDestination) {
        shouldDhowBottomBar.value = when(currentDestination){
            Routes.LoginScreen::class.qualifiedName, Routes.SingUpScreen::class.qualifiedName ->false
            else ->true
        }
    }
    LaunchedEffect(firebaseAuth.currentUser) {
        Log.d("Firebase", "App: current User: ${firebaseAuth.currentUser} ${firebaseAuth.currentUser?.uid}")
    }
    Log.d("Firebase", "App: current User: ${firebaseAuth.currentUser} ${firebaseAuth.currentUser?.uid}")


    val bottomNavItem = listOf(
        BottomNavItem("Home", Icons.Default.Home, Icons.Outlined.Home),
        BottomNavItem("Wishlist", Icons.Default.Favorite, Icons.Outlined.Favorite),
        BottomNavItem("Cart", Icons.Default.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavItem("Profile", Icons.Default.Person, Icons.Outlined.Person),
    )


    var startScreen = if(firebaseAuth.currentUser ==null){
        Log.d("Firebase", "App: current User in if: ${firebaseAuth.currentUser} ${firebaseAuth.currentUser?.uid}")

        SubNavigation.LoginSignUpScreen
    }
    else{
        Log.d("Firebase", "App: current User in else: ${firebaseAuth.currentUser} ${firebaseAuth.currentUser?.uid}")

        SubNavigation.MainHomeScreen
    }

    Scaffold(bottomBar = {
        if(shouldDhowBottomBar.value)   {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                AnimatedBottomBar(
                    selectedItem=selectedItem,
                    itemSize = bottomNavItem.size,

                    containerColor = Color.Transparent,
                    indicatorColor = colorResource(R.color.mediumBeige),
                    indicatorDirection = IndicatorDirection.BOTTOM,
                    indicatorStyle = IndicatorStyle.FILLED
                ) {
                    bottomNavItem.forEachIndexed { index, navigationItem->

                        BottomBarItem(
                            selected = index==selectedItem,
                            onClick = {
                                selectedItem=index
                                when(index){
                                    0->navController.navigate(Routes.HomeScreen)
                                    1->navController.navigate(Routes.WishListScreen)
                                    2->navController.navigate(Routes.CartScreen)
                                    3->navController.navigate(Routes.AccountScreen)
                                }
                            },
                            label = navigationItem.name,
                            textColor = colorResource(R.color.darkBeige),
                            iconColor = colorResource(R.color.darkBeige) ,
                            containerColor = Color.Transparent,
                            imageVector = if (index==selectedItem) navigationItem.icon
                            else navigationItem.unselectedIcon
                        )
                    }
                }
            }
        }
    }) { innerPadding->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .windowInsetsPadding(WindowInsets.navigationBars)

                .padding()
        ){
            NavHost(navController=navController,
                startDestination = startScreen
            ){
                navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.LoginScreen){
                    composable<Routes.LoginScreen> {
                        SignInScreen(navController=navController)
                    }
                    composable<Routes.SingUpScreen> {
                        SignUpScreen(navController=navController)
                    }
                }

                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen) {
                    composable<Routes.HomeScreen> {
                        HomeScreenUi(navController=navController, firebaseAuth = firebaseAuth)
                    }
                    composable<Routes.WishListScreen> {
                        GetAllFavScreenUi(navController=navController)
                    }
                    composable<Routes.CartScreen> {
                        CartScreen(navController=navController)
                    }
                    composable<Routes.ProfileScreen> {
                        ProfileScreenUi(navController=navController, firebaseAuth=firebaseAuth)
                    }
                    composable<Routes.AccountScreen> {
                        UserAccountScreenUI(navController,firebaseAuth= firebaseAuth)
                    }
                }
                composable<Routes.SeeAllProducts> {
                    GetAllProductScreen(navController=navController)
                }
                composable<Routes.AllCategoriesScreen> {
                    AllCategoriesScreen(navController=navController)
                }
                composable<Routes.ShippingInformationScreen> {
                    ShippingInformationScreenUI(navController=navController)
                }
                composable<Routes.EachProductDetailsScreen> {
                    val product: Routes.EachProductDetailsScreen=it.toRoute()
                    EachProductDetailsScreen(navController=navController, productId = product.productId)
                }
                composable<Routes.EachCategoryItemsScreen> {
                    val category: Routes.EachCategoryItemsScreen=it.toRoute()
                    EachCategoryProductScreenUi(
                        navController=navController,
                        categoryId = category.categoryId,
                        categoryName = category.categoryName
                    )
                }
//                composable<Routes.CheckOutScreen> {
//                    val orderData:Routes.CheckOutScreen=it.toRoute()
//                    CheckOutScreen(navController=navController, ordersData = orderData.ordersData)
//                }

                composable<Routes.CheckOutScreen> {
                    Log.d("Navigation", "App: inside composabel, about to get data")
                    val orderData: OrdersData? = navController.previousBackStackEntry?.savedStateHandle?.get<OrdersData>("orderData")
                    Log.d("Navigation", "App: got the order data: ${orderData}\ntrying to open checkout")
                    CheckOutScreen(navController=navController, ordersData = orderData?: OrdersData())
                    Log.d("Navigation", "checkout")

                }

                composable<Routes.OrderConfirmationScreen> {

                    val ordersData = navController.previousBackStackEntry?.savedStateHandle?.get<OrdersData>("orderData")
                    Log.d("Order Confirmation", "App: orderdata - ${ordersData}")
                    Log.d("Order Confirmation", "OrderConfirmationScreenUI: order items ${ordersData?.orderItems}")
                    OrderConfirmationScreenUI(ordersData=ordersData?: OrdersData(), navController=navController)
                }


            }
        }



    }



}

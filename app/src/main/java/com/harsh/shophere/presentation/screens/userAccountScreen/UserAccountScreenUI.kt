package com.harsh.shophere.presentation.screens.userAccountScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.harsh.shophere.R
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.features.user.presentation.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountScreenUI(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    firebaseAuth: FirebaseAuth
) {
    val userState = userViewModel.userState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        firebaseAuth.currentUser?.uid?.let { uid ->
            userViewModel.loadUser(uid)
        }
    }

    if (userState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    else if (userState.value.errorMessage != null) {
        Text(text = userState.value.errorMessage!!)
    }
    else if (userState.value.user != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                        ) {

                            Text(
                                text = "Account",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Icon",
                                Modifier.clickable(onClick = {navController.popBackStack()})
                            )
                        }

                    },

                )
            },
            modifier = Modifier
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                ProfileSection(
                    userState.value.user!!.firstName+" "+userState.value.user!!.lastName,
                    userImage = userState.value.user!!.profileImage
                )



                val sections=listOf(
                    SectionNavigationElement(
                        "Order History",
                        icon = Icons.Default.ShoppingCart,
                        null
                     ) ,
                    SectionNavigationElement(
                        "Payment Methods",
                        icon = Icons.Default.Menu,
                        null
                    ),
                    SectionNavigationElement(
                        "Profile Settings",
                        icon = Icons.Outlined.Person,
                        Routes.ProfileScreen
                    ),
                    SectionNavigationElement(
                        "Wishlist",
                        icon = Icons.Default.FavoriteBorder,
                        Routes.WishListScreen
                    ),

                )
                sections.forEach { section ->
                    SectionItem(section.screenName,section.icon!!){
                        section.navigation?.let {
                            navController.navigate(section.navigation)
                        }
                    }
                }

            }


        }

    }
}


private data class SectionNavigationElement <T>(
    val screenName: String,
    val icon: ImageVector? =null,
    val navigation: T
)


@Composable
private fun ProfileSection(
    userName: String,
    userImage:String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if(userImage.isEmpty()){
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .shadow(4.dp)

            )
        }
        else{
            AsyncImage(
                model = userImage,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                ,

            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            userName,
            color = colorResource(R.color.darkBeige),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,

        )

    }
}



@Composable
private fun SectionItem(
    sectionName:String,
    sectionIcon: ImageVector,
    onClick:()-> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF9F9F9))
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick=onClick)
    ) {
        OutlinedButton(
            onClick = { onClick },
            border = null,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(),
            modifier = Modifier
                .padding(end = 16.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color(0xFFEFEDED), shape = RoundedCornerShape(8.dp))
        ) {
            Icon(
                imageVector = sectionIcon ,
                contentDescription = "",
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .width(24.dp)
                    .height(24.dp)
                    .clickable(onClick=onClick)
            )
        }
        Text(
            sectionName,
            color = Color(0xFF161411),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "",
            modifier = Modifier
                .padding(2.dp)
                .width(24.dp)
                .height(24.dp)
        )
    }
}


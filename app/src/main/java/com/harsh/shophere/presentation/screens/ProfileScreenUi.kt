package com.harsh.shophere.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.google.firebase.auth.FirebaseAuth
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.presentation.Utils.LogOutAlertDialog
import com.harsh.shophere.presentation.navigation.SubNavigation
import com.harsh.shophere.presentation.viewModels.ShopViewModel


@Composable
fun ProfileScreenUi(
    shopViewModel: ShopViewModel=hiltViewModel(),
    navController: NavController,
    firebaseAuth: FirebaseAuth
) {
    LaunchedEffect(true){
        shopViewModel.getUserById(firebaseAuth.currentUser!!.uid)
    }

    val profileScreenState = shopViewModel.profileScreenState.collectAsStateWithLifecycle()
    val upDateScreenState = shopViewModel.updateScreenState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val showDialog = remember {
        mutableStateOf(false)
    }

    val isEditing = remember {
        mutableStateOf(false)
    }


    val firstName =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.firstName) }
    val lastName =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.lastName) }
    val email = remember { mutableStateOf(profileScreenState.value.userData?.userData?.email) }
    val phoneNumber =
        remember { mutableStateOf(profileScreenState.value.userData?.userData?.phoneNumber) }
    val address = remember { mutableStateOf(profileScreenState.value.userData?.userData?.address) }

    LaunchedEffect(profileScreenState.value.userData) {
        profileScreenState.value.userData?.userData?.let { userData ->
            firstName.value = userData.firstName
            lastName.value = userData.lastName
            email.value = userData.email
            phoneNumber.value = userData.phoneNumber
            address.value = userData.address
        }
    }


    if (upDateScreenState.value.userData != null) {
        Toast.makeText(context, upDateScreenState.value.userData, Toast.LENGTH_SHORT).show()
    } else if (upDateScreenState.value.errorMessage != null) {

        Toast.makeText(context, upDateScreenState.value.errorMessage, Toast.LENGTH_SHORT).show()

    } else if (upDateScreenState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

        if (profileScreenState.value.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else if (profileScreenState.value.errorMessage != null) {
            Text(text = profileScreenState.value.errorMessage!!)
        } else if (profileScreenState.value.userData != null) {
        Scaffold(


        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Start)
                ) {
                    Image(
                        painter = painterResource(R.drawable.user_profile_image),
                        contentDescription = "User Profile Icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, color = colorResource(R.color.darkBeige), CircleShape)
                    )

                }

                Spacer(modifier = Modifier.size(16.dp))

                Row() {
                    OutlinedTextField(
                        value = firstName.value.toString(),
                        onValueChange = { firstName.value = it },
                        modifier = Modifier.weight(1f),
                        readOnly = !isEditing.value,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(R.color.darkBeige),
                            focusedBorderColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(text = "First Name") }
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    OutlinedTextField(
                        value = lastName.value.toString(),
                        onValueChange = { lastName.value = it },
                        modifier = Modifier.weight(1f),
                        readOnly = !isEditing.value,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = colorResource(R.color.darkBeige),
                            focusedBorderColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(text = "Last Name") }
                    )

                }

                Spacer(modifier = Modifier.size(16.dp))

                OutlinedTextField(
                    value = email.value.toString(),
                    onValueChange = { email.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = !isEditing.value,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(R.color.darkBeige),
                        focusedBorderColor = colorResource(R.color.darkBeige)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Email") }
                )

                Spacer(modifier = Modifier.size(16.dp))

                OutlinedTextField(
                    value = phoneNumber.value.toString(),
                    onValueChange = { phoneNumber.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = !isEditing.value,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(R.color.darkBeige),
                        focusedBorderColor = colorResource(R.color.darkBeige)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Phone Number") }
                )
                Spacer(modifier = Modifier.size(16.dp))

                OutlinedTextField(
                    value = address.value.toString(),
                    onValueChange = { address.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = !isEditing.value,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(R.color.darkBeige),
                        focusedBorderColor = colorResource(R.color.darkBeige)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Address") }
                )

                Spacer(Modifier.size(16.dp))
                OutlinedButton(
                    onClick = { showDialog.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.darkBeige))
                ) {
                    Text("Log Out")

                }
                if (showDialog.value) {
                    LogOutAlertDialog(
                        onDismiss = { showDialog.value = false },
                        onConfirm = {
                            firebaseAuth.signOut()
                            navController.navigate(SubNavigation.LoginSignUpScreen)
                        }
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))
                if (!isEditing.value) {
                    OutlinedButton(
                        onClick = {
                            isEditing.value = !isEditing.value
                        },

                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text("Edit Profile")
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            val updatedUserData = UserData(
                                firstName = firstName.value?:"",
                                lastName = lastName.value?:"",
                                email = email.value?:"",
                                phoneNumber = phoneNumber.value?:"",
                                address = address.value?:"",
                                userId=profileScreenState.value.userData!!.userData.userId,
                            )
                            shopViewModel.updateUserData(
                                updatedUserData
                            )
                            isEditing.value = false


                        },

                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text("Save Profile")
                    }

                }
            }

        }
    }


}
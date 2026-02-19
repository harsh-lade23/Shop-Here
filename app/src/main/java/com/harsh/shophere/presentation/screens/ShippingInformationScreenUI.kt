package com.harsh.shophere.presentation.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.Address
import com.harsh.shophere.domain.models.OrdersData
import com.harsh.shophere.domain.models.ShippingDetails
import com.harsh.shophere.features.user.presentation.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingInformationScreenUI(
    navController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel()

) {

    val context = LocalContext.current
    val name = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }
    val village = remember { mutableStateOf("") }
    val state = remember { mutableStateOf("") }
    val contact = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val pinCode = remember { mutableStateOf("") }

    val shippingDetails =remember { mutableStateOf(ShippingDetails()) }



    val userState=userViewModel.userState.collectAsStateWithLifecycle()


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    BackHandler() {
        navController.previousBackStackEntry?.savedStateHandle?.set("orderData", null)
        navController.popBackStack()
    }



    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Your Delivery Details",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.previousBackStackEntry?.savedStateHandle?.set("orderData", null)
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->



        when{
            userState.value.isLoading->{
                Box (Modifier.fillMaxSize(),
                    contentAlignment=Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
            userState.value.errorMessage!=null->{
                // Error state handled silently, will show on button click if needed
            }
        }

        // Observe success by checking if addresses were updated after save

        LaunchedEffect(userState.value.shippingAddresses) {
            if (userState.value.shippingAddresses.isNotEmpty() && 
                shippingDetails.value.name.isNotEmpty()) {
                Toast.makeText(context, "Shipping details saved successfully", Toast.LENGTH_SHORT).show()
                val existingData= navController.previousBackStackEntry?.savedStateHandle?.get<OrdersData>("orderData")
                val updatedOrderData=existingData?.copy(
                    shippingDetails = shippingDetails.value
                )
                navController.previousBackStackEntry?.savedStateHandle?.set("orderData", updatedOrderData)
                navController.popBackStack()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 50.dp)
                ) {
                    Text(
                        text = "General Information",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 29.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                    )

                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)

                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFC0C0C0),
                            unfocusedBorderColor = Color(0xFFC0C0C0),
                            cursorColor = colorResource(R.color.darkBeige),
                            focusedTextColor = colorResource(R.color.darkBeige),
                            unfocusedTextColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        keyboardOptions = KeyboardOptions()
                    )
                    Spacer(Modifier.height(23.dp))
                    Text(
                        text = "Contact",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                    )

                    OutlinedTextField(
                        value = contact.value,
                        onValueChange = { contact.value = it },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)

                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFC0C0C0),
                            unfocusedBorderColor = Color(0xFFC0C0C0),
                            cursorColor = colorResource(R.color.darkBeige),
                            focusedTextColor = colorResource(R.color.darkBeige),
                            unfocusedTextColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(23.dp))
                    Text(
                        text = "Email address",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                    )

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)

                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFC0C0C0),
                            unfocusedBorderColor = Color(0xFFC0C0C0),
                            cursorColor = colorResource(R.color.darkBeige),
                            focusedTextColor = colorResource(R.color.darkBeige),
                            unfocusedTextColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                }
                Spacer(modifier = Modifier.height(30.dp))
                Column {
                    Text(
                        text = "Shipping Address",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 29.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = "Street",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                    )

                    OutlinedTextField(
                        value = street.value,
                        onValueChange = { street.value = it },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)

                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFC0C0C0),
                            unfocusedBorderColor = Color(0xFFC0C0C0),
                            cursorColor = colorResource(R.color.darkBeige),
                            focusedTextColor = colorResource(R.color.darkBeige),
                            unfocusedTextColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        keyboardOptions = KeyboardOptions()
                    )
                    Spacer(Modifier.height(23.dp))
                    Text(
                        text = "Village",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                    )

                    OutlinedTextField(
                        value = village.value,
                        onValueChange = { village.value = it },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)

                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFC0C0C0),
                            unfocusedBorderColor = Color(0xFFC0C0C0),
                            cursorColor = colorResource(R.color.darkBeige),
                            focusedTextColor = colorResource(R.color.darkBeige),
                            unfocusedTextColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        keyboardOptions = KeyboardOptions()
                    )
                    Spacer(Modifier.height(23.dp))
                    Text(
                        text = "State",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                    )

                    OutlinedTextField(
                        value = state.value,
                        onValueChange = { state.value = it },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)

                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFC0C0C0),
                            unfocusedBorderColor = Color(0xFFC0C0C0),
                            cursorColor = colorResource(R.color.darkBeige),
                            focusedTextColor = colorResource(R.color.darkBeige),
                            unfocusedTextColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(Modifier.height(23.dp))
                    Text(
                        text = "Country",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                    )

                    OutlinedTextField(
                        value = country.value,
                        onValueChange = { country.value = it },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)

                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFC0C0C0),
                            unfocusedBorderColor = Color(0xFFC0C0C0),
                            cursorColor = colorResource(R.color.darkBeige),
                            focusedTextColor = colorResource(R.color.darkBeige),
                            unfocusedTextColor = colorResource(R.color.darkBeige)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        keyboardOptions = KeyboardOptions()
                    )
                    Spacer(Modifier.height(23.dp))



                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = "City",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp,
                                modifier = Modifier
                                    .padding(start = 10.dp, bottom = 7.dp)
                            )

                            OutlinedTextField(
                                value = city.value,
                                onValueChange = { city.value = it },

                                textStyle = TextStyle(fontSize = 14.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFC0C0C0),
                                    unfocusedBorderColor = Color(0xFFC0C0C0),
                                    cursorColor = colorResource(R.color.darkBeige),
                                    focusedTextColor = colorResource(R.color.darkBeige),
                                    unfocusedTextColor = colorResource(R.color.darkBeige)
                                ),
                                shape = RoundedCornerShape(4.dp),
                                keyboardOptions = KeyboardOptions()
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = "Pin Code",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp,
                                modifier = Modifier
                                    .padding(start = 15.dp, bottom = 7.dp)
                            )

                            OutlinedTextField(
                                value = pinCode.value,
                                onValueChange = { pinCode.value = it },

                                textStyle = TextStyle(fontSize = 14.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFC0C0C0),
                                    unfocusedBorderColor = Color(0xFFC0C0C0),
                                    cursorColor = colorResource(R.color.darkBeige),
                                    focusedTextColor = colorResource(R.color.darkBeige),
                                    unfocusedTextColor = colorResource(R.color.darkBeige)
                                ),
                                shape = RoundedCornerShape(4.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    }

                }
            }
            Button(
                onClick = {
                    /*TODO- pay.invoke()*/
                    /*TODO - Save */

                    if(
                        state.value.isEmpty() ||
                        country.value.isEmpty() ||
                        city.value.isEmpty() ||
                        pinCode.value.isEmpty() ||
                        name.value.isEmpty() ||
                        contact.value.isEmpty() ||
                        email.value.isEmpty()
                    ){
                        Toast.makeText(context, "Please fill the required fields", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        shippingDetails.value=
                            ShippingDetails(
                                address = Address(
                                    country = country.value,
                                    state = state.value,
                                    city = city.value,
                                    village = village.value,
                                    pinCode = pinCode.value,
                                    street = street.value
                                ),
                                name = name.value,
                                contactNo = contact.value.toString(),
                                email = email.value
                            )

                        userViewModel.saveShippingAddress(
                            shippingDetails =shippingDetails.value
                        )
                    }
                },
                modifier = Modifier
                    .weight(0.11f)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.darkBeige))
            ) {
                Text(
                    text = "Save Shipping Information",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

        }
    }
}


package com.harsh.shophere.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.harsh.shophere.R
import com.harsh.shophere.domain.models.UserData
import com.harsh.shophere.presentation.Utils.CustomTextField
import com.harsh.shophere.presentation.Utils.SuccessAlertDialog
import com.harsh.shophere.presentation.navigation.Routes
import com.harsh.shophere.presentation.navigation.SubNavigation
import com.harsh.shophere.presentation.viewModels.ShopViewModel


@Composable
fun SignUpScreen(navController: NavHostController,
                 shopViewModel: ShopViewModel=hiltViewModel()
) {

    val state=shopViewModel.signUpScreenState.collectAsStateWithLifecycle()

    if(state.value.isLoading){
        Box(modifier=Modifier.fillMaxSize()){
            CircularProgressIndicator(
                modifier=Modifier.align(Alignment.Center)
            )
        }
    }
    else if(state.value.errorMessage !=null){
        Box(modifier= Modifier.fillMaxSize()){
            Text(text=state.value.errorMessage!!)
        }
    }
    else if(state.value.userData!=null){
        SuccessAlertDialog {
            navController.navigate(SubNavigation.MainHomeScreen)
        }
    }
    else{
        val context = LocalContext.current

        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "SignUp",
                fontSize = 26.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(alignment = Alignment.Start)

                    .padding(vertical = 17.dp, horizontal = 3.dp)
            )

            CustomTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
            CustomTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.apply { KeyboardType.Email }
            )

            CustomTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number",
                leadingIcon = Icons.Default.Call,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation()
            )
            CustomTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )

            Button(
                onClick = {
                    if (
                        firstName.isEmpty() || lastName.isEmpty() ||
                        email.isEmpty() || phoneNumber.isEmpty() ||
                        password.isEmpty() || confirmPassword.isEmpty()
                    ) {
                        Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show()
                    } else {
                        if (password == confirmPassword){

                            val userData= UserData(
                                firstName=firstName,
                                lastName=lastName,
                                phoneNumber=phoneNumber,
                                email=email,
                            )

                            Toast.makeText(context, "Creating Account", Toast.LENGTH_SHORT).show()

                            shopViewModel.createUser(userData, password)


                        }
                        else{
                            Toast.makeText(context, "Password and Confirm Password not match", Toast.LENGTH_SHORT).show()
                        }

                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 9.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.orange))
            ) {
                Text("SignUp",
                    color=colorResource(R.color.white)
                )

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text("Already have an account?")
                TextButton(onClick = {
                    navController.navigate(Routes.LoginScreen)
                }
                ) {
                    Text("Login",
                        color=colorResource(R.color.orange))
                }
            }




        }

    }




}
package com.harsh.shophere.presentation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.harsh.shophere.features.auth.presentation.AuthViewModel
import com.harsh.shophere.features.auth.presentation.state.LoginUiState

@Composable
fun SignInScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val state = authViewModel.loginState.collectAsStateWithLifecycle()
    val showDialog= remember{
        mutableStateOf(false)
    }


    val context = LocalContext.current


    if(state.value.isLoading){
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator(
                modifier=Modifier.align(Alignment.Center)
            )
        }
    }
    else if(state.value.errorMessage!=null){
        Box(modifier= Modifier.fillMaxSize()){
            Text(
                text=state.value.errorMessage!!
            )
        }

    }
    else if(state.value.userData!=null){
        SuccessAlertDialog(
            onClick = {
                navController.navigate(SubNavigation.MainHomeScreen)
            }
        )
    }
    else {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Login",
                fontSize = 26.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(alignment = Alignment.Start)

                    .padding(vertical = 17.dp, horizontal = 3.dp)
            )

            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )

            Spacer(modifier = Modifier.padding(8.dp))

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation(),

                )


            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill the details", Toast.LENGTH_SHORT).show()
                    } else {

                        val userData= UserData(
                            email=email,
                        )
                        Toast.makeText(context, "Checking Credentials...", Toast.LENGTH_SHORT).show()
                        authViewModel.login(userData, password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.orange)),
                border = BorderStroke(1.dp, colorResource(R.color.orange))
            ) {
                Text(
                    text = "Login",
                    color = colorResource(R.color.white)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text("Don't have an account?")
                TextButton(onClick = {
                    navController.navigate(Routes.SingUpScreen)
                }
                ) {
                    Text(
                        "Sign Up",
                        color = colorResource(R.color.orange)
                    )
                }
            }



        }
    }

}
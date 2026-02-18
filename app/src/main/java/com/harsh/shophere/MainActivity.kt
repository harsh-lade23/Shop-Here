package com.harsh.shophere

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.harsh.shophere.data.repo.RepositoryImplementation
import com.harsh.shophere.presentation.Utils.ImageBanner
import com.harsh.shophere.presentation.navigation.App
import com.harsh.shophere.ui.theme.ShopHereTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var repositoryImplementation: RepositoryImplementation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopHereTheme {
                MainScreen(firebaseAuth)


            }
        }
    }

    @Composable
    fun MainScreen(
        firebaseAuth: FirebaseAuth
    ){
        val showSplash= remember{mutableStateOf(true)}
        LaunchedEffect(Unit) {
            Handler(Looper.getMainLooper()).postDelayed({
                showSplash.value=false
            },3000)
        }

        if(showSplash.value){
            SplashScreen()
        }
        else{
            App(firebaseAuth)
        }


    }

    @Composable
    fun SplashScreen(){
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color= colorResource(R.color.black)),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(R.drawable.shopping_lady),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(300.dp)
                )
                BasicText(
                    text = "Welcome to the Clothing Store",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color=colorResource(R.color.white),
                        fontSize=18.sp,
                        textAlign = TextAlign.Center
                    ),
                )

            }
        }
    }
}

package com.example.artmart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.artmart.marketplace.Inventory
import com.example.artmart.marketplace.Market
import kotlinx.coroutines.delay

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "splash_screen"){
                composable("splash_screen"){
                    SplashScreen(navController = navController)
                }

                //screen select
                composable("select_screen"){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ){
                        Column {
                            Text(text = "Click the button to begin your art adventure", color = Color.Black, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(5.dp))
                            Button(onClick = { startTheAuthenticationActivity()},
                                colors = ButtonDefaults.outlinedButtonColors(Color.Blue)
                                ) {
                                Text(text = "Sign Up", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun SplashScreen(navController: NavController){
        val scale  = remember {
            androidx.compose.animation.core.Animatable(0f)
        }
        //animation effect
        LaunchedEffect(key1 = true){
            scale.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = {
                        OvershootInterpolator(4f).getInterpolation(it)
                    }
                )
            )
            delay(3000L)
            // after time elapses
            navController.navigate("select_screen")
        }
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
                ){
            Image(painter = painterResource(id = R.drawable.artmart),
                contentDescription = "Splash Screen Image",
            modifier = Modifier.scale(scale.value)
            )
        }
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            Text(
                text = "Handmade Happiness",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp)
            )
        }

    }


    private fun startTheAuthenticationActivity() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)

    }
}







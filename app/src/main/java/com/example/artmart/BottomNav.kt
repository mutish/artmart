package com.example.artmart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.artmart.BottomNavigationItems.Home.route
import com.example.artmart.tools.ClassResources

class BottomNav : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomScreen()

        }
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun BottomScreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController)},
        content = {
            Box(modifier = Modifier.padding(it)) {
                Navigation(navController = navController)
            }
        }, backgroundColor = colorResource(id = R.color.white))
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController , startDestination = BottomNavigationItems.Home.route){
        composable(BottomNavigationItems.Home.route){
            MainScreen()



        }

        composable(BottomNavigationItems.Class.route){
            ClassResources {}
        }

    }
}
@Composable
fun BottomNavigationBar(navController: NavController){
    val items = listOf(BottomNavigationItems.Home, BottomNavigationItems.Class)
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.Blue,
        modifier = Modifier.padding(5.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        //tag for active page/ requested page
        val currentRoute = navBackStackEntry?.destination?.route
        //loop our items for the bottom navigation
        items.forEach{
            //
            BottomNavigationItem(
                modifier = Modifier.padding(3.dp),
                selected = false,
                icon = { Icon(painterResource(id = it.icon), contentDescription = it.title )}, label = {Text(it.title)}, alwaysShowLabel = true,
                selectedContentColor = Color.Black, unselectedContentColor = Color.Black.copy(0.4f),
                onClick = {
//                    handle navigation here
                    navController.navigate(it.route){
                        navController.graph.startDestinationRoute?.let {
                            // create stack i.e. whats on top of the stack
                            // and whats below it
                            // create the navigation protocol
                            popUpTo(route){
                                saveState = true
                            }
                        }
                        // avoid multiple copies of the same view
                        // especially when reselecting the same item
                        launchSingleTop = true
                        // restoring the state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

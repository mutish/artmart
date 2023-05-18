package com.example.artmart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.artmart.marketplace.Inventory
import com.example.artmart.marketplace.Testing
import com.example.artmart.ui.theme.ArtmartTheme

class HomeActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtmartTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        MainScreen()

                    }
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {

                        composable("home") {
                            val mContext = LocalContext.current
                            mContext.startActivity(Intent(mContext, BottomNav::class.java))


                        }
                        composable("details/id={id}?name={name}",
                            arguments = listOf(
                                navArgument("id"){
                                    type = NavType.LongType
                                },
                                navArgument("name"){
                                    type = NavType.StringType
                                    nullable = true
                                }),
                        ){
                            //inside this composable , I can pick the shared details
                            val arguments = requireNotNull(it.arguments)
                            val id = arguments.getLong("id")
                            val name = arguments.getString("name")

                        }


                    }
                }

            }

        }
    }

}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalFoundationApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backgroundImage = painterResource(R.drawable.nightsky)
    Scaffold(topBar =
    { TopAppBar(title = { Text(text = "Welcome to ArtMart") }) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = backgroundImage,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            val categories = allcategories


            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(categories.size) { index ->
                    CategoryCard(categories[index]) {
                        navController.navigate("category/${categories[index].name}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.align(Alignment.BottomCenter)) {
                Box() {
                    Gallerybutton(navController = navController)
                }
                Spacer(modifier = Modifier.width(5.dp))

                Box() {
                    SaleButton(navController = navController)
                }

                Spacer(modifier = Modifier.width(5.dp))

            }


        }

    }

}

@Composable
fun SaleButton(navController: NavController) {
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        // Handle any result if needed
    }
    Button(
        onClick = {
            val intent = Intent(navController.context, Testing::class.java)
            activityLauncher.launch(intent)
        },
        modifier = Modifier.padding(10.dp),
        colors = ButtonDefaults.textButtonColors(Color.White)
    ) {
        Text(text = "Visit the store!")
    }
}

@Composable
fun Gallerybutton(navController: NavController) {
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        // Handle any result if needed
    }
    Button(
        onClick = {
            val intent = Intent(navController.context, Inventory::class.java)
            activityLauncher.launch(intent)
        },
        modifier = Modifier.padding(10.dp),
        colors = ButtonDefaults.textButtonColors(Color.White)
    ) {
        Text(text = "Go to gallery")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryCard(categories: Categories, onClick: (Categories) -> Unit) {
    val showDialog = remember {
        mutableStateOf(false)
    }
    val onClick = {
        showDialog.value = true
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(categories) }
            .clip(MaterialTheme.shapes.medium),
        elevation = 8.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = categories.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = categories.name,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            ViewCard(showDialog = showDialog.value, onDismiss = {showDialog.value = false},categories = categories)
        }
    }
}

@Composable
fun ViewCard(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    categories: Categories
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        if (categories != null) {
                            Image(
                                painter = rememberImagePainter(data = categories.imageRes),
                                contentDescription = "Thumbnail",
                                modifier = Modifier
                                    .size(300.dp)
                            )
                        }
                    }



                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {

                        item {
                            if (categories != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = 4.dp
                                ) {
                                    Column() {
                                        Text(
                                            text = " ${categories.name}",
                                            modifier = Modifier.padding(16.dp)
                                        )

                                        Text(
                                            text = "History: ${categories.info}",
                                            modifier = Modifier.padding(16.dp)
                                        )

                                    }
                                }
                            }
                        }



                    }


                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }
    }
}
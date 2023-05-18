package com.example.artmart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.artmart.marketplace.Inventory
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
                    Column() {
                        MainScreen()
                    }
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {

                            val mContext = LocalContext.current
                            mContext.startActivity(Intent(mContext, BottomNav::class.java))
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
fun CustomImageDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    categories: Categories
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
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
                                painter = rememberImagePainter(data = categories.),
                                contentDescription = "Goods Photo",
                                modifier = Modifier
                                    .size(300.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    if (customer != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = 4.dp
                        ) {
                            Text(
                                text = customer.name,
                                style = MaterialTheme.typography.h3,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp)
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
                            if (customer != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = 4.dp
                                ) {
                                    Column() {
                                        Text(
                                            text = "Phone Number: ${customer.phone_Number}",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                        Text(
                                            text = "Current Location: ${customer.current_Location}",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }

                                }
                            }
                        }


                        item {
                            if (customer != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = 4.dp
                                ) {
                                    Column() {
                                        Text(
                                            text = "Target Location: ${customer.target_Location}",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                        Text(
                                            text = "Goods Type: ${customer.goods_Type}",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }

                                }
                            }
                        }


                        item {
                            if (customer != null) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = 4.dp
                                ) {
                                    Column() {
                                        Text(
                                            text = "Goods Nature: ${customer.goods_Nature}",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                        Text(
                                            text = "Proposed Price: ${customer.proposed_Price}",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }

                                }
                            }
                        }


                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row() {
                            Button(onClick = {


                                simulateCheckout(customer)

                            }) {

                                Text(text = "Checkout")
                            }

                            Button(
                                onClick = { onDismiss() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Text("OK", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CategoryCard(categories: Categories, onClick: (Categories) -> Unit) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { onClick(categories) }
                .clip(MaterialTheme.shapes.medium),
            elevation = 8.dp,
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
            }
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




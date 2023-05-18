package com.example.artmart.marketplace

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import com.example.artmart.R
import com.example.artmart.favorites.Favourites
import com.example.artmart.favorites.FavouritesDAO
import com.example.artmart.favorites.FavouritesDatabase
import com.example.artmart.models.ArtSaleClass
import com.example.artmart.ui.theme.ArtmartTheme
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class Inventory : ComponentActivity() {
    private val favouritesDatabase by lazy { FavouritesDatabase.getDatabase(this).favouritesDao()}
    @SuppressLint ("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtmartTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(topBar = {
                        TopAppBar(backgroundColor = MaterialTheme.colors.background,
                        title = {
                            Text(
                                text = "Art Inventory",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                                style = TextStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)

                            )

                        })
                    }) {
                        Column(modifier = Modifier.padding(it)) {
                            //mutablelistof artobjects
                            var artList = mutableStateListOf<ArtSaleClass?>()
                            //firebase instance
                            val firebaseDatabase = FirebaseDatabase.getInstance()
                            val databaseReference = firebaseDatabase.getReference("ArtDB")
                            //reading data values...use addChildEventListener
                            databaseReference.addChildEventListener(object : ChildEventListener {
                                override fun onChildAdded(
                                    snapshot: DataSnapshot,
                                    previousChildName: String?
                                ) {
                                    // the method is called when a new record is added hence adding the item to the list
                                    val artproduct = snapshot.getValue(ArtSaleClass::class.java)
                                    artList.add(artproduct)
                                }

                                override fun onChildChanged(
                                    snapshot: DataSnapshot,
                                    previousChildName: String?
                                ) {
                                   // method is called when a new record is added
                                }

                                override fun onChildRemoved(snapshot: DataSnapshot) {
                                  //method called when a record/ child is removed
                                }

                                override fun onChildMoved(
                                    snapshot: DataSnapshot,
                                    previousChildName: String?
                                ) {
                                    //when child is moved
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    //if any error occurs
                                    Toast.makeText(this@Inventory,"Error !!,"+error.message,Toast.LENGTH_LONG).show()
                                    Log.d("FirebaseReading","Error is" + error.message)
                                    Log.d("FirebaseReading","Error is" + error.details)
                                    Log.d("FirebaseReading","Error is" + error.code)
                                }
                            })
                            //call to composable to display UI
                            listOfProducts(LocalContext.current,
                                artList,lifecycleScope, favouritesDatabase)


                        }

                    }

                }

                }

            }
        }
    }

@Composable
fun listOfProducts(context: Context,
                   artList: SnapshotStateList<ArtSaleClass?>,
lifecycleScope: LifecycleCoroutineScope, favouritesDatabase: FavouritesDAO) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gallery!!",
            modifier = Modifier.padding(10.dp),
            style = TextStyle(
                color = Color.Black, fontSize = 30.sp, fontFamily = FontFamily.Cursive
            ), fontWeight = FontWeight.Bold
        )

        ShowFave(
            favouritesDatabase = favouritesDatabase,
            lifecycleScope = lifecycleScope
        )

        LazyColumn {
            items(artList) { artproduct ->
                //custom ui
                ArtCard(product = artproduct!!, context,lifecycleScope,favouritesDatabase)
            }

        }


    }
}

@Composable
fun ShowFave(favouritesDatabase: FavouritesDAO, lifecycleScope: LifecycleCoroutineScope) {
    var showDialog by remember{ mutableStateOf(false) }
    var favouriteList by remember {
        mutableStateOf(emptyList<Favourites>())
    }
    Button(onClick = {
        // fetching data from the room database and setting the state of our alert box
        showDialog = true
        // get our list
        lifecycleScope.launch{
            favouriteList = favouritesDatabase.getFavs().first()
        }

    }) {
        Text(text = "View Favourites")
    }
    var dialogHeight  = 600.dp
    if (showDialog){
        AlertDialog(
            onDismissRequest = {  showDialog = false},
            title = { Text(text = "Your World")},
            text = {
                Box(modifier = Modifier
                    .height(dialogHeight)
                    .fillMaxWidth() ){
                    LazyColumn(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()){
                        items(favouriteList) {
                            Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = it.favouriteName, fontWeight = FontWeight.Bold)
                                    Text("Image download link : ${it.favouriteImage}")
//                                    Text("Product Seller : ${it.favouriteContact}")
//                                    Text("Product Price : ${it.favouritePrice}")
                                }
                            }
                        }
                    }
                }


            } ,
            confirmButton = {
                Button(onClick = { showDialog = false}) {
                    Text(text = "Close")
                }
            }
        )
    }

}







@Composable
    fun ArtCard(
        product: ArtSaleClass,
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        favouritesDatabase: FavouritesDAO
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {  }, elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // replace with a dynamic image
                Image(
                    painter = rememberImagePainter(data = product.image),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp)),
                )

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Artist: ${product.artist}",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Light,
                fontSize = 12.sp)
//                Text(text = "Seller Price: ${product.price}")
                Spacer(modifier = Modifier.height(5.dp))
                // define state to track loading process
                var isLoading by remember {
                    mutableStateOf(false)
                }
                // row
                Row() {
                    Button(onClick = {
                        isLoading = true
                        // get the current time and date
                        val newFavAdded  = Date()
                        // add product to favourite
                        val newFav = Favourites(product.artid,product.title,product.phonenumber,product.image
                            ,product.price,newFavAdded)
                        // adding the product to the room db
                        lifecycleScope.launch{
                            favouritesDatabase.addFav(newFav)
                            delay(3000)
                            isLoading = false
                        }
                    }) {
                        if (isLoading){
                            LoadingProgress()
//                        CircularProgressIndicator()
                        } else {
                            Icon(painter = painterResource(id = R.drawable.like), contentDescription =null )
                        }

                    }

                }
            }
        }
}

@Composable
fun LoadingProgress() {
    val strokeWidth = 5.dp
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = Color.Black,
            strokeWidth = strokeWidth
        )
    }
}






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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmart.R
import com.example.artmart.marketplace.models.ArtSaleClass
import com.example.artmart.ui.theme.ArtmartTheme
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class Inventory : ComponentActivity() {
    @SuppressLint ("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtmartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(topBar = {
                        TopAppBar(backgroundColor = Color.Cyan,
                        title = {
                            Text(
                                text = "Art Inventory",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.White

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
                                artList
                            )

                        }

                    }

                }

            }
        }
    }
}
@Composable
fun listOfProducts(context: Context, artList: SnapshotStateList<ArtSaleClass?>){
    Column(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .background(Color.White),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Products World",
            modifier = Modifier.padding(10.dp),
            style = TextStyle(
                color = Color.Black, fontSize = 16.sp
            ), fontWeight = FontWeight.Bold
        )
        LazyColumn{
            items(artList){ artproduct ->
                //custom ui
                ArtCard(product = artproduct!!)
            }


        }

    }

}
@Composable
fun ArtCard(product: ArtSaleClass){
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(), elevation = 4.dp) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // replace with a dynamic image
            Image(
                painter =  painterResource(id = R.drawable.sales),
                contentDescription = "Product Image", modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Text(text = product.title,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Seller Contact: ${product.phonenumber}")
            Text(text = "Seller Price: ${product.price}")
        }
    }
}


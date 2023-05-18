package com.example.artmart

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmart.marketplace.Inventory
import com.example.artmart.marketplace.ui.theme.ArtmartTheme
import com.example.artmart.models.ArtSaleClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class Testing : ComponentActivity() {
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtmartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(topBar = {
                        TopAppBar(backgroundColor = Color.Black,
                            title = {
                                Text(
                                    text = "My Dashboard",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                        )
                    }) {
                        Column(modifier = Modifier.padding(it)) {
                            //get firebase instance and reference
                            val firebaseDatabase = FirebaseDatabase.getInstance()
                            val databaseReference = firebaseDatabase.getReference("ArtDB")
                            //storage ref
                            //storage service instance
                            val storage = Firebase.storage
                            storageReference = storage.reference.child("Artworks")
                            ArtForm(LocalContext.current, databaseReference,storageReference)

                        }

                    }

                }

            }
        }
    }
}

@Composable
fun ArtForm(context: Context,
            databaseReference: DatabaseReference,
            storageReference:StorageReference) {
    //storing users input
    val image = remember { mutableStateOf(TextFieldValue()) }
    val title = remember { mutableStateOf(TextFieldValue()) }
    val artist = remember { mutableStateOf(TextFieldValue()) }
    val number = remember { mutableStateOf(TextFieldValue()) }
    val price = remember { mutableStateOf(TextFieldValue()) }

    val bimages = painterResource(id = R.drawable.nightsky)

    Column(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .background(Color.White),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Share your content to the store",modifier = Modifier.padding(7.dp), style= TextStyle(
            color = Color.Black, fontSize = 23.sp
        ), fontWeight = FontWeight.Bold, fontFamily = FontFamily.Cursive)

        // text fields
        TextField(value = title.value, onValueChange = {title.value = it},
            placeholder = { Text(text = "Title",color = Color.Black)}, modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp), singleLine = true
        )
        Spacer(modifier = Modifier.height(5.dp))
        TextField(value = artist.value, onValueChange = {artist.value = it},
            placeholder = { Text(text = "Name of Artist", color = Color.Black)}, modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp), singleLine = true
        )
        Spacer(modifier = Modifier.height(5.dp))
        //Gallerybutton to allow input from user's gallery
        //1.A state to hold our upload value
        //2.A launcherforActivityResult instance: start an activity : access other apps
        // holding uri file

        val selectedUri = remember { mutableStateOf<Uri?>(null) }
        //ref to launcher
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
            //save selection path to state variable
            selectedUri.value = it
        }
        // Gallerybutton for onclick event to select a file
        Button(onClick = {
            launcher.launch("image/*")
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Place image here")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
//            push our data to the realtime database
            // use the push method to generate a unique key for the record
            //first we send the image/file to the storage bucket
            selectedUri.value?.let {
                val imageName = "image_${System.currentTimeMillis()}"
                val imageRef = storageReference.child(imageName)
                imageRef.putFile(it).addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {
                        val imagePath = it.toString()
                        val newProduct_Reference = databaseReference.push()
                        // key / unique identifier
                        val artid = newProduct_Reference.key
                        val artClass = artid?.let {
                            ArtSaleClass(
                                it, imagePath, title.value.text, artist.value.text, number.value.text,
                                price.value.text
                            )
                        }

                        // we use a class in firebase called the addValueEventListener
                        databaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                newProduct_Reference.setValue(artClass)
                                Toast.makeText(
                                    context,
                                    "Item has been added successfully!!,",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d("Item Push", snapshot.toString())
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    context,
                                    "Product failed to be  added!!,",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d("Product Push", error.message)
                            }

                        })
                    }
                }
            }

        }, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), enabled = selectedUri.value != null) {

            Text(text = "Share your piece", modifier = Modifier.padding(5.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){

            val activitylauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()){ activityResult ->

            }
            Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                onClick = {
                    val intent = Intent(context, Inventory::class.java)
                    activitylauncher.launch(intent)
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "View Gallery", modifier = Modifier.padding(start = 8.dp))

                }

            }

            //logout

            Button(colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    changeUi(context,activitylauncher)
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Logout", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

fun changeUi(
    context: Context,
    activitylauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val intent = Intent(context, MainActivity::class.java)
    activitylauncher.launch(intent)
}


@Composable
fun imageUploader(activity: ComponentActivity, storageReference: StorageReference,databaseReference: DatabaseReference){
    //state to hold image uri
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    //activity result launcher to start image picker
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        selectedImageUri.value = it
    }

    Column {
        //Gallerybutton to launch image picker
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "select image")
        }
    }
}


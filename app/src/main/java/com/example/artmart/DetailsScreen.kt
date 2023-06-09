package com.example.artmart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(id:Long, name: String?,onNavigateUp: () -> Unit){
    val cartegories = allcategories.get(index = id.toInt() -1)
    val scrollState = rememberScrollState()
    Scaffold{
        Column(Modifier.padding(it)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp),
            ) {
                IconButton(onClick = onNavigateUp) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Go Back")
                }
            }
            Column(modifier = Modifier.verticalScroll(state = scrollState)) {


                Image(
                    painterResource(cartegories.imageRes), contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f), contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1, text = cartegories.name
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1, text = cartegories.info
                    )
                }
            }
        }
    }
}
//@Composable
//fun DetailsScreen(id: Long,name:String?, onNavigateUp: () -> Unit) {
//    //from the list of articles, we use the id to filter & only show the clicked item
//    val category = allcategories.first {
//        it.id == id
//    }
//    Scaffold {
//        Column(Modifier.padding(it)) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(vertical = 10.dp)
//            ) {
//                IconButton(onClick = onNavigateUp) {
//                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Go back")
//                }
//            }
//
//
//            Image(
//                painterResource(category.imageRes), contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(16f / 9f), contentScale = ContentScale.Crop
//            )
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Column(
//                Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 16.dp)
//            ) {
//                Text(
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center, style = MaterialTheme.typography.body1,
//                    text = category.name
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//
//                BoxWithConstraints {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .padding(bottom = 16.dp)
//                            .background(color = Color.Black) ,
//                        content = {
//                            item{
//                                Text(
//                                    text = category.name,
//                                    style = MaterialTheme.typography.body1,
//                                    modifier = Modifier.padding(16.dp))
//                            }
//                            item{
//                                Text(
//                                    text = category.info,
//                                    style = MaterialTheme.typography.body1,
//                                    modifier = Modifier.padding(16.dp))
//                            }
//
//
//                        })
//
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//            }
//        }
//    }
//}
//
//

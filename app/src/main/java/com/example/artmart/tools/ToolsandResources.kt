package com.example.artmart.tools

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artmart.R
import com.example.artmart.tools.ui.theme.ArtmartTheme

class ToolsandResources : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtmartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                backgroundColor = Color.Yellow,
                                title = {
                                    Text(
                                        text = "Tools and Resources",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Left,
                                        color = Color.White,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            )
                        }
                    ) {
                        Column {
                            ClassResources(onClick = {})
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterialApi::class)
@Composable

fun ClassResources(onClick: () -> Unit) {
    val uriHandler = LocalUriHandler.current

        Card(onClick = onClick) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.brush2),
                    contentDescription = "Thumbnail",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Get online courses at an affordable price",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(3.dp))
                ClickableText(
                    text = AnnotatedString("Click here to pick your course to begin your path in art"),
                    style = TextStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                    onClick = {
                        uriHandler.openUri("https://courses.laimoon.com/kenya/media-creative-and-design/art-and-sculpture/painting")
                    },
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Card(onClick = onClick) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.personalstudy),
                            contentDescription = "Thumbnail",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Do you prefer self-taught",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        ClickableText(
                            text = AnnotatedString("Click here to get started"),
                            style = TextStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                            onClick = {
                                uriHandler.openUri("https://www.brendanmeachen.com/soloartist")
                            },
                            modifier = Modifier.padding(16.dp)
                        )


                    }
                }
            }
        }
}







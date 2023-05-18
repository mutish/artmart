package com.example.artmart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onDetailsClick: (id:Long)-> Unit) {
    Scaffold {
        LazyColumn(contentPadding = it) {
            item {
                MainScreen()
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
            items(allcategories) {
                CategoryCard(it, onClick = {
                    onDetailsClick(it.id)
                })
            }
        }
    }
}
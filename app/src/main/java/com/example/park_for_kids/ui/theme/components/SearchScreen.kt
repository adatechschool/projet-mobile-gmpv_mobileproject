//package com.example.park_for_kids.ui.theme.components
//
//import androidx.compose.runtime.Composable
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.park_for_kids.data.model.Playground
//import com.example.park_for_kids.network.RetrofitClient
//import kotlinx.coroutines.launch
//
//@Composable
//fun SearchScreen() {
//    var query by remember { mutableStateOf("") }
//    var results by remember { mutableStateOf<List<Playground>>(emptyList()) }
//    val coroutineScope = rememberCoroutineScope()
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        SearchBar(
//            query = query,
//            onQueryChange = { newQuery ->
//                query = newQuery
//                coroutineScope.launch {
//                    try {
//                        results = RetrofitClient.retrofitService.getResults(query)
//                    } catch (e: Exception) {
//                        // Handle errors
//                    }
//                }
//            }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        LazyColumn {
//            items(results) { playground ->
//                Text(
//                    text = playground.metaNameCom,
//                    style = MaterialTheme.typography.body1,
//                    modifier = Modifier.padding(8.dp)
//                )
//                // Display other relevant information
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun SearchScreenPreview() {
//    MyAppTheme {
//        SearchScreen()
//    }
//}

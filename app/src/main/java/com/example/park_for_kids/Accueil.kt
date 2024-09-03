package com.example.park_for_kids

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Accueil(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "ParKids")
        Text(text = "Trouvez un parc pour enfants près de chez vous")

        Button(onClick = {
            navController.navigate(Routes.Home)
        }) {
            Text(text = "EXPLORER")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
    val navController = rememberNavController()
    Accueil(navController)
}
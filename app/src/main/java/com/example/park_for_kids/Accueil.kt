package com.example.park_for_kids

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Accueil(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        AccueilText()

        AccueilImage(contentDescription = "Garçon sur une balançoire",
            painterResourcesId = R.drawable.lovepik)
        Button(onClick = {
            navController.navigate(Routes.Home,)
        }) {
            Text(text = "EXPLORER")
        }
    }
}

@Composable
fun AccueilText() {
    Column(
        //verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        Text(text = "ParKids", fontSize = 40.sp, fontWeight = FontWeight.Bold)
        Text(text = "Trouvez un parc pour enfants près de chez vous", textAlign = TextAlign.Center, fontSize = 20.sp, modifier = Modifier.padding(20.dp))
    }
}

@Composable
fun AccueilImage(contentDescription: String, painterResourcesId: Int) {
    val image = painterResource(painterResourcesId)
    Image(
        painter = image,
        contentDescription = contentDescription,
        contentScale = ContentScale.Inside
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
    val navController = rememberNavController()
    Accueil(navController)
}
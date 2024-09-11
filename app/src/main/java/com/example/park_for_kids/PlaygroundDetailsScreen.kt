package com.example.park_for_kids

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun PlaygroundDetailsScreen(navController: NavController, playgroundId: String) {
    // Logic and UI for displaying playground details
    // Example: Fetch playground details based on the ID
    Text(text = "Détails pour le parc avec l'ID : $playgroundId")

    Button(onClick = { navController.popBackStack() }) {
        Text("Retour")
    }
}
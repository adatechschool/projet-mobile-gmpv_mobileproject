package com.example.park_for_kids

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.park_for_kids.data.model.HomeViewModel
import com.example.park_for_kids.network.PlaygroundResponse

@Composable
fun Home(){
    // Obtenez une instance du ViewModel
    val viewModel: HomeViewModel = viewModel()
    val searchQuery = viewModel.searchQuery.value
    val playgrounds = viewModel.playgrounds.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    // Interface principale avec recherche
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.searchQuery.value = it },
            label = { Text("Rechercher par ville, code postal, département ou région") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.searchPlaygrounds() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Rechercher")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Affichage de l'état du chargement, des erreurs ou des résultats
        if (isLoading) {
            CircularProgressIndicator()
        } else if (errorMessage != null) {
            Text("Erreur: $errorMessage", color = MaterialTheme.colorScheme.error)
        } else {
            PlaygroundList(playgrounds)  // Affichage de la liste des aires de jeux
        }
    }
}


@Composable
fun PlaygroundList(playgrounds: List<PlaygroundResponse.Playground>) {
    LazyColumn {
        items(playgrounds) { playground ->
            PlaygroundItem(playground)
        }
    }
}

@Composable
fun PlaygroundItem(playground: PlaygroundResponse.Playground) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Nom: ${playground.name ?: "Non disponible"}", style = MaterialTheme.typography.bodyLarge)
        Text("Ville: ${playground.meta_name_com}", style = MaterialTheme.typography.bodyMedium)
        Text("Code Postal: ${playground.meta_code_com}", style = MaterialTheme.typography.bodyMedium)
        Text("Département: ${playground.meta_name_dep}", style = MaterialTheme.typography.bodyMedium)
        Text("Région: ${playground.meta_name_reg}", style = MaterialTheme.typography.bodyMedium)

        // Afficher les coordonnées géographiques si elles sont disponibles
        playground.meta_geo_point?.let { geoPoint ->
            Text("Longitude: ${geoPoint.lon}, Latitude: ${geoPoint.lat}", style = MaterialTheme.typography.bodyMedium)
        }
        HorizontalDivider()
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview(){
    val navController = rememberNavController()
    Home()
}
package com.example.park_for_kids

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.park_for_kids.data.model.HomeViewModel
import com.example.park_for_kids.network.PlaygroundResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.example.park_for_kids.ui.theme.*

@Composable
fun Home(navController: NavController){
    // Obtenez une instance du ViewModel
    val viewModel: HomeViewModel = viewModel()
    val searchQuery = viewModel.searchQuery.value
    val playgrounds = viewModel.playgrounds.value
    val totalCount = viewModel.totalCount.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    // Interface principale avec recherche
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.searchQuery.value = it },
            label = { Text("Recherchez par ville") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
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
            // Afficher le nombre total de parcs
            if (totalCount != null) {
                Text(
                    "Nombre total de parcs : $totalCount",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,)
            }
            PlaygroundList(playgrounds, navController)  // Affichage de la liste des aires de jeux
        }
    }
}


@Composable
fun PlaygroundList(playgrounds: List<PlaygroundResponse.Playground>, navController: NavController) {
    LazyColumn {
        items(playgrounds) { playground ->
            PlaygroundItem(playground, navController)
        }
    }
}

@Composable
fun PlaygroundItem(playground: PlaygroundResponse.Playground, navController: NavController) {
    val context = LocalContext.current
    var address by remember { mutableStateOf("Chargement...") }

    // Utilisation d'une coroutine pour charger l'adresse en arrière-plan
    LaunchedEffect(playground) {
        playground.meta_geo_point?.let { geoPoint ->
            address = getAddressFromCoordinates(context, geoPoint.lat, geoPoint.lon)
        }    }
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Nom: ${playground.name ?: "Non disponible"}",
        style = MaterialTheme.typography.bodyLarge,
        color = Secondary,
        modifier = Modifier.clickable {
            // Naviguer vers la page des détails avec l 'ID du parc
            playground.meta_osm_id?.let { osmId ->
                navController.navigate("playgroundDetails/$osmId")
        }
    }
        )
        Text(
            "Ville: ${playground.meta_name_com}",
            style = MaterialTheme.typography.bodyMedium,
            color = Secondary
            )
        // Affiche l'adresse récupérée et permet de cliquer pour ouvrir dans Google Maps
        Text(
            text = "Adresse: $address",
            style = MaterialTheme.typography.bodyMedium,
            color = Secondary,
            modifier = Modifier.clickable {
                playground.meta_geo_point?.let { geoPoint ->
                    OpenInGoogleMaps(context, address)
                }
            }
        )
        HorizontalDivider()
    }
}

suspend fun getAddressFromCoordinates(context: Context, latitude: Double?, longitude: Double?): String {
    if (latitude == null || longitude == null) return "Coordonnées non disponibles"

    val geocoder = Geocoder(context, Locale.getDefault())
    return withContext(Dispatchers.IO) {
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                addresses[0].getAddressLine(0) ?: "Adresse non trouvée"
            } else {
                "Adresse non trouvée"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Erreur lors de la récupération de l'adresse"
        }
    }
}

fun OpenInGoogleMaps(context: Context, address: String?) {
    if (!address.isNullOrEmpty()) {
        val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}") // Utilisation de l'adresse pour la requête
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps") // Ouvrir explicitement dans Google Maps
        context.startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    Home(navController)
}
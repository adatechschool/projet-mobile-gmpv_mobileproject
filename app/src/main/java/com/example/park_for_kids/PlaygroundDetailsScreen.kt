package com.example.park_for_kids

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.park_for_kids.data.model.OneParkViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.park_for_kids.network.PlaygroundResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PlaygroundDetailsScreen(navController: NavController, playgroundId: String, playground: PlaygroundResponse.Playground? = null) {

    val viewModel: OneParkViewModel = viewModel()
    val playgroundState by viewModel.playground.collectAsState() // Renommé pour éviter la confusion
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Si playground n'est pas fourni, récupérer via l'API
    LaunchedEffect(playgroundId) {
        if (playground == null) {
            viewModel.getPlaygroundById(playgroundId)
        }
    }

    val currentPlayground = playground ?: playgroundState

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espace entre les éléments
        ) {
            Spacer(modifier = Modifier.height(200.dp))  // Ajoute de l'espace au-dessus pour décaler la colonne

            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                errorMessage != null -> {
                    Text(
                        "Erreur: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                else -> {
                    currentPlayground?.let { park ->
                        Text(
                            "Nom: ${park.name ?: "Non disponible"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            "Ville: ${park.meta_name_com}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "Département: ${park.meta_name_dep}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "Région: ${park.meta_name_reg}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "Age minimum: ${park.min_age ?: "Non disponible"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "Horaires d'ouverture: ${park.opening_hours ?: "Non disponible"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        // Afficher la carte OSM si l'URL est disponible
                        park.meta_osm_url?.let { osmUrl ->
                            WebViewComponent(osmUrl)
                        }
                    }
                }
            }

            // Bouton "Retour" bien placé dans le Box
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
//                    .align(Alignment.BottomCenter)
                    .padding(30.dp)
                    .fillMaxWidth() // Remplir la largeur disponible
                    .height(48.dp) // Hauteur standard du bouton
            ) {
                Text("Retour")
            }
        }
    }
}

    @Composable
    fun WebViewComponent(url: String) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient =
                        WebViewClient() // Assure que les liens s'ouvrent dans la WebView
                    loadUrl(url)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp) // Ajoute du padding autour de la WebView
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun PlaygroundDetailsScreenPreview() {
        // Simuler un objet Playground pour le Preview
        val playground = PlaygroundResponse.Playground(
            name = "Aire de jeux du Parc Central",
            min_age = 3,
            surface = null,
            max_age = null,
            opening_hours = "08:00 - 20:00",
            meta_name_com = "Paris",
            meta_name_dep = "Paris",
            meta_code_dep = null,
            meta_name_reg = "Île-de-France",
            meta_code_reg = null,
            meta_geo_point = null,
            meta_osm_id = "1024276870",
            meta_osm_url = null
        )

        PlaygroundDetailsScreen(
            navController = rememberNavController(),
            playgroundId = "1024276870",
            playground = playground
        )
    }
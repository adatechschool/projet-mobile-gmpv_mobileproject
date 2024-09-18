package com.example.park_for_kids

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.park_for_kids.data.model.OneParkViewModel
import com.example.park_for_kids.network.PlaygroundResponse
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PlaygroundDetailsScreen(navController: NavController, playgroundId: String, playground: PlaygroundResponse.Playground? = null) {

    val viewModel: OneParkViewModel = viewModel()
    val playgroundState by viewModel.playground.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    // Initialiser la configuration osmdroid
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "ParKinfos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp, bottom = 16.dp) // Ajout de padding
            )

            Spacer(modifier = Modifier.height(16.dp))

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

                        Spacer(modifier = Modifier.height(32.dp))

                        // Afficher la carte OSM si les coordonnées sont disponibles
                        park.meta_geo_point?.let { geoPoint ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp) // Ajouter du padding horizontal
                                    .align(Alignment.CenterHorizontally) // Centrer horizontalement
                            ) {
                                // Utilisation des coordonnées géographiques du parc
                                OsmMapComponent(latitude = geoPoint.lat, longitude = geoPoint.lon)
                            }
                        }
                    }
                }
            }
        }

        // Bouton retour en bas
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                Text("Retour")
            }
        }
    }
}

@Composable
fun OsmMapComponent(latitude: Double, longitude: Double) {
    val context = LocalContext.current

    // Vérification des permissions d'accès à la localisation
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Demande de permissions ici
        ActivityCompat.requestPermissions(
            (context as Activity),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1
        )
        return
    }

    // Création et configuration de la carte osmdroid
    AndroidView(
        factory = { ctx ->
            val mapView = MapView(ctx)
            mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            mapView.controller.setZoom(19.0)
            val startPoint = GeoPoint(latitude, longitude)
            mapView.controller.setCenter(startPoint)

            // Ajouter un marqueur à l'emplacement
            val marker = org.osmdroid.views.overlay.Marker(mapView)
            marker.position = startPoint
            marker.icon = ctx.getDrawable(R.drawable.marker_icon) // Assurez-vous d'avoir un drawable pour l'icône du marqueur
            marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)

            mapView
        },
        modifier = Modifier
            .fillMaxWidth() // Utiliser toute la largeur disponible
            .aspectRatio(1f) // Carte carrée
            .padding(bottom = 96.dp) // Espace pour ne pas chevaucher le bouton et les informations
    )
}

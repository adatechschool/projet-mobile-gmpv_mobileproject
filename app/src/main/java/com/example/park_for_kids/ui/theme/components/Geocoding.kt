package com.example.park_for_kids.ui.theme.components

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

// Fonction pour obtenir l'adresse à partir des coordonnées GPS
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

// Fonction pour ouvrir l'adresse dans Google Maps
fun openInGoogleMaps(context: Context, address: String?) {
    if (!address.isNullOrEmpty()) {
        val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}") // Utilisation de l'adresse pour la requête
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps") // Ouvrir explicitement dans Google Maps
        context.startActivity(intent)
    }
}

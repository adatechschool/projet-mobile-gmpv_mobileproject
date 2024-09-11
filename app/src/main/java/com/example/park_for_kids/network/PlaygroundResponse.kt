package com.example.park_for_kids.network

data class PlaygroundResponse(
    val total_count: Int,
    val results: List<Playground>
) {

    // Classe pour une aire de jeux
    data class Playground(
        val name: String?,
        val surface: String?,
        val min_age: Int?,
        val max_age: Int?,
        val opening_hours: String?,
        val meta_name_com: String,  // Ville
        val meta_name_dep: String,  // Département
        val meta_code_dep: String,  // Code Département
        val meta_name_reg: String,  // Région
        val meta_code_reg: String,  // Code Région
        val meta_geo_point: MetaGeoPoint?,  // Coordonnées géographiques (lat, lon)
        val meta_osm_id: String?,
        val meta_osm_url: String?
    )

    // Classe pour les informations géographiques
    data class MetaGeoPoint(
        val lon: Double,
        val lat: Double
    )
}

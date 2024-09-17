package com.example.park_for_kids.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PlaygroundApiService {

    // Fonction pour obtenir une liste de parcs par ville
    @GET("api/explore/v2.1/catalog/datasets/osm-france-playground/records")
    suspend fun getPlaygrounds(@Query("where") query: String): PlaygroundResponse

    // Fonction pour obtenir un parc par osmId
    @GET("api/explore/v2.1/catalog/datasets/osm-france-playground/records")
    suspend fun getPlaygroundById(@Query("where") query: String): PlaygroundResponse

}
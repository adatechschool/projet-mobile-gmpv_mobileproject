package com.example.park_for_kids.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PlaygroundApiService {

    @GET("api/explore/v2.1/catalog/datasets/osm-france-playground/records")
    suspend fun getPlaygrounds(@Query("where") query: String): PlaygroundResponse

}
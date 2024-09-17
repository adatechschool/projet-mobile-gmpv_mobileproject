package com.example.park_for_kids.data.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.park_for_kids.network.PlaygroundResponse
import com.example.park_for_kids.network.RetrofitClient
import kotlinx.coroutines.launch
import java.net.URLEncoder

class HomeViewModel : ViewModel() {
    var searchQuery = mutableStateOf("")
    var playgrounds = mutableStateOf<List<PlaygroundResponse.Playground>>(emptyList())
    var totalCount = mutableStateOf<Int?>(null)
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    private val apiService = RetrofitClient.apiService

    fun searchPlaygrounds() {
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                // Appel de l'API pour obtenir les aires de jeux
                val query = formatQuery(searchQuery.value)
                val response = apiService.getPlaygrounds(query)

                println("API Response: $response")

                // On récuère le nombre de parcs de la ville
                totalCount.value = response.total_count
                // On récupère la liste des résultats dans 'results'
                playgrounds.value = response.results

                println("Total count: ${response.total_count}, Playgrounds: ${response.results}")

            } catch (e: Exception) {
                errorMessage.value = "Erreur: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
    private fun formatQuery(cityName: String): String {
        // Encadre la ville entre guillemets et encode les caractères spéciaux
        val encodedCityName = URLEncoder.encode(cityName, "UTF-8")
        return "meta_name_com:\"$encodedCityName\""
    }
}
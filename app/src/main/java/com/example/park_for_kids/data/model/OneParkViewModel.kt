package com.example.park_for_kids.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.park_for_kids.network.PlaygroundResponse
import com.example.park_for_kids.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OneParkViewModel : ViewModel() {
    private val _playground = MutableStateFlow<PlaygroundResponse.Playground?>(null)
    val playground: StateFlow<PlaygroundResponse.Playground?> = _playground

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val apiService = RetrofitClient.apiService

    fun getPlaygroundById(osmId: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // Construire la requête pour filtrer par osmId
                val query = "meta_osm_id:\"$osmId\""
                // Appel de l'API pour obtenir les détails du parc
                val response = apiService.getPlaygroundById(query)

                // Assurer que la réponse contient des résultats
                if (response.results.isNotEmpty()) {
                    _playground.value = response.results.first()
                } else {
                    _errorMessage.value = "Parc non trouvé"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
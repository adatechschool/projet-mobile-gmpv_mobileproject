package com.example.park_for_kids

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.park_for_kids.ui.theme.park_for_kidsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            park_for_kidsTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    // NavHost définit les différentes routes de navigation
    NavHost(navController = navController, startDestination = Routes.Accueil) {
        // Page principale (Home)
        composable(Routes.Accueil) {
            Accueil(navController = navController)
        }
        // Page Home
        composable(Routes.Home) {
            Home(navController = navController)
        }
        // Page PlaygroundDetails
        composable(Routes.PlaygroundDetails) { backStackEntry ->
            val osmId = backStackEntry.arguments?.getString("osmId")
            osmId?.let {
                PlaygroundDetailsScreen(navController = navController, playgroundId = it)
            }
        }
    }
}


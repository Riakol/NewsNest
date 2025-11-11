package com.riakol.newsnest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.riakol.newsnest.ui.navigation.NewsNavGraph
import com.riakol.newsnest.ui.theme.NewsNestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsNestTheme {
                val navController = rememberNavController()
                NewsNavGraph(navController = navController)
            }
        }
    }
}
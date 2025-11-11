package com.riakol.newsnest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.riakol.newsnest.ui.components.NewsFeedScreen
import com.riakol.newsnest.ui.screens.ArticleScreen

const val NEWS_FEED_ROUTE = "news_feed"
const val ARTICLE_ROUTE = "article/{url}"

@Composable
fun NewsNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NEWS_FEED_ROUTE
    ) {
        composable(NEWS_FEED_ROUTE) {
            NewsFeedScreen(
                navigateToArticle = { url ->
                    navController.navigate("article/${url.encode()}")
                }
            )
        }

        composable(
            route = ARTICLE_ROUTE,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url").orEmpty().decode()
            ArticleScreen(url = url, onBack = { navController.popBackStack() })
        }
    }
}

private fun String.encode(): String = this.replace("/", "%2F")
private fun String.decode(): String = this.replace("%2F", "/")
package com.riakol.newsnest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.riakol.newsnest.ui.components.NewsFeedScreen
import com.riakol.newsnest.ui.screens.ArticleScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

const val NEWS_FEED_ROUTE = "news_feed"
const val ARTICLE_ROUTE = "article/{article_id_encoded}"

@Composable
fun NewsNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NEWS_FEED_ROUTE) {
        composable(NEWS_FEED_ROUTE) {
            NewsFeedScreen(navController = navController)
        }
        composable(
            route = ARTICLE_ROUTE,
            arguments = listOf(navArgument("article_id_encoded") {
                type = NavType.StringType
                nullable = false
            })
        ) { backStackEntry ->
            val encodedId = checkNotNull(backStackEntry.arguments?.getString("article_id_encoded"))
            val articleId = URLDecoder.decode(encodedId, StandardCharsets.UTF_8.toString())
            ArticleScreen(
                navController = navController,
                articleId = articleId
            )
        }
    }
}
package com.riakol.newsnest.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.riakol.newsnest.ui.navigation.ARTICLE_ROUTE
import com.riakol.newsnest.ui.navigation.BottomNavigationBar
import com.riakol.newsnest.viewmodel.NewsFeedViewModel
import com.riakol.newsnest.viewmodel.NewsUiState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NewsFeedScreen(
    navController: NavHostController
) {
    val viewModel: NewsFeedViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            NewsTopAppBar(onSearch = { query ->
                viewModel.loadNews(query = query)
            })
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is NewsUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is NewsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(uiState.news) { article ->
                            NewsCard(
                                news = article,
                                onArticleClick = { id ->
                                    val encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString())
                                    Log.d("NewsFeedScreen", "Article ID: $encodedId")
                                    navController.navigate("article/$encodedId")
                                }
                            )
                        }
                    }
                }
                is NewsUiState.Error -> {
                    Text(
                        text = "Error: ${uiState.message}",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
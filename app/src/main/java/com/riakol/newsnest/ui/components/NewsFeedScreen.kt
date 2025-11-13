package com.riakol.newsnest.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.riakol.newsnest.ui.navigation.BottomNavigationBar
import com.riakol.newsnest.viewmodel.NewsFeedViewModel
import com.riakol.newsnest.viewmodel.NewsUiState

@Composable
fun NewsFeedScreen(
    navController: NavHostController
) {
    val viewModel: NewsFeedViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedTopic by viewModel.selectedTopic.collectAsState()
    val topics = viewModel.topics

    Scaffold(
        topBar = {
            NewsTopAppBar(
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChange
            )
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TopicChips(
                topics = topics,
                selectedTopic = selectedTopic,
                onTopicChange = viewModel::onTopicChange
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is NewsUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is NewsUiState.Success -> {
                        if (state.news.isEmpty()) {
                            Text(
                                text = "No results found.",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                items(state.news, key = { it.id }) { article ->
                                    NewsCard(
                                        news = article,
                                        onArticleClick = { id ->
                                            val encodedId = id.replace("/", "~")
                                            navController.navigate("article/$encodedId")
                                        }
                                    )
                                }
                            }
                        }
                    }
                    is NewsUiState.Error -> {
                        Text(
                            text = "Error: ${state.message}",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicChips(
    topics: List<String>,
    selectedTopic: String?,
    onTopicChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(topics) { topic ->
            val isSelected = topic == selectedTopic
            FilterChip(
                selected = isSelected,
                onClick = { onTopicChange(topic) },
                label = { Text(topic) },
                leadingIcon = {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Selected",
                            modifier = Modifier.height(18.dp)
                        )
                    }
                }
            )
        }
    }
}
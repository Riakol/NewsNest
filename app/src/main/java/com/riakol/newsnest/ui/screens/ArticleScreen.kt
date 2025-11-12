package com.riakol.newsnest.ui.screens

import android.annotation.SuppressLint
import android.graphics.Color
import android.webkit.WebView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.riakol.newsnest.viewmodel.ArticleUiState
import com.riakol.newsnest.viewmodel.ArticleViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    navController: NavHostController,
    articleId: String,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    LaunchedEffect(articleId) {
        viewModel.loadArticle(articleId)
    }

    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = (uiState as? ArticleUiState.Success)?.article?.section ?: "Article",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is ArticleUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ArticleUiState.Success -> {
                    val article = uiState.article
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = article.imageUrl,
                            contentDescription = "Article image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = article.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp)
                            )

                            Text(
                                text = formatDisplayDate(article.publishedAt),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            val isDark = isSystemInDarkTheme()
                            val styledHtmlBody = getStyledHtml(article.body ?: "No content.", isDark)

                            ArticleWebView(styledHtmlBody)
                        }
                    }
                }
                is ArticleUiState.Error -> {
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

/**
 * Вспомогательная функция для форматирования даты
 */
private fun formatDisplayDate(isoDate: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoDate)
        // Форматируем в локализованный вид, например "12 ноября 2023 г."
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
            .withLocale(Locale("ru", "RU"))
        zonedDateTime.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale("ru", "RU")))
    } catch (e: Exception) {
        isoDate.substringBefore("T")
    }
}

/**
 * Composable-обертка для WebView
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun ArticleWebView(htmlBody: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = false
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                setBackgroundColor(Color.TRANSPARENT)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(
                null,
                htmlBody,
                "text/html",
                "UTF-8",
                null
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}


/**
 * Вспомогательная функция для добавления CSS-стилей к HTML
 */
private fun getStyledHtml(body: String, isDark: Boolean): String {
    val textColor = if (isDark) "#E0E0E0" else "#121212"
    val linkColor = if (isDark) "#82B1FF" else "#0056D3"
    val backgroundColor = if (isDark) "#121212" else "#FFFFFF"

    return """
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
                    background-color: $backgroundColor;
                    color: $textColor;
                    line-height: 1.6;
                    font-size: 1.1em; 
                }
                p {
                    margin-bottom: 1em; /* Отступ между параграфами */
                }
                a {
                    color: $linkColor;
                    text-decoration: none; 
                }
                img, figure, video {
                    max-width: 100%;
                    height: auto;
                    border-radius: 8px; /* Скругляем углы у изображений в статье */
                }
            </style>
        </head>
        <body>
            $body
        </body>
        </html>
    """.trimIndent()
}
package com.example.nobellaureatesclient.presentation.details

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nobellaureatesclient.domain.model.Laureate
import com.example.nobellaureatesclient.domain.model.NobelPrize
import com.example.nobellaureatesclient.presentation.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrizeDetailsScreen(
    onBack: () -> Unit,
    viewModel: PrizeDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()

    val currentPrize = (state as? UiState.Success)?.data
    val isFavorite = currentPrize?.id?.let { it in favoriteIds } == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали премии") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (currentPrize != null) {
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (isFavorite) "Убрать из избранного" else "В избранное",
                                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when (val s = state) {
                is UiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }

                is UiState.Error -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Ошибка: ${s.message}",
                            color = MaterialTheme.colorScheme.error,
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = viewModel::retry) { Text("Повторить") }
                    }
                }

                is UiState.Success -> PrizeContent(prize = s.data)
            }
        }
    }
}

@Composable
private fun PrizeContent(prize: NobelPrize) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "${prize.year} · ${prize.category.displayName}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        prize.motivation?.takeIf { it.isNotBlank() }?.let {
            Text(text = it, style = MaterialTheme.typography.bodyLarge)
        }

        HorizontalDivider()

        Text(
            text = "Лауреаты",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        if (prize.laureates.isEmpty()) {
            Text(
                text = "Лауреаты не указаны",
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            prize.laureates.forEach { laureate ->
                LaureateBlock(laureate = laureate)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun LaureateBlock(laureate: Laureate) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = laureate.fullName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        val years = buildString {
            laureate.birthDate?.let { append("род. $it") }
            laureate.deathDate?.let {
                if (isNotEmpty()) append(" · ")
                append("ум. $it")
            }
        }
        if (years.isNotBlank()) {
            Text(text = years, style = MaterialTheme.typography.bodySmall)
        }
        laureate.affiliation?.takeIf { it.isNotBlank() }?.let {
            Text(text = "Аффилиация: $it", style = MaterialTheme.typography.bodyMedium)
        }
        laureate.share?.let {
            Text(text = "Доля: 1/$it", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

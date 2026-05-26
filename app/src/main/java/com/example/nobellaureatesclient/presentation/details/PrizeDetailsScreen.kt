package com.example.nobellaureatesclient.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.nobellaureatesclient.domain.model.Laureate
import com.example.nobellaureatesclient.domain.model.NobelPrize
import com.example.nobellaureatesclient.presentation.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrizeDetailsScreen(
    onBack: () -> Unit,
    viewModel: PrizeDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали премии") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val s = state) {
                is UiState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is UiState.Error -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Ошибка: ${s.message}",
                            color = MaterialTheme.colorScheme.error
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "${prize.awardYear} · ${prize.categoryFullName.ifBlank { prize.category.displayName }}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        prize.dateAwarded?.let {
            Text(text = "Дата награждения: $it", style = MaterialTheme.typography.bodyMedium)
        }
        prize.prizeAmount?.let {
            Text(text = "Сумма премии: $it SEK", style = MaterialTheme.typography.bodyMedium)
        }

        HorizontalDivider()

        prize.laureates.forEach { laureate ->
            LaureateBlock(laureate = laureate)
            HorizontalDivider()
        }
    }
}

@Composable
private fun LaureateBlock(laureate: Laureate) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!laureate.portraitUrl.isNullOrBlank()) {
                AsyncImage(
                    model = laureate.portraitUrl,
                    contentDescription = laureate.fullName,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(16.dp))
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = laureate.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                laureate.country?.let {
                    Text(text = "Страна: $it", style = MaterialTheme.typography.bodyMedium)
                }
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
            }
        }
        if (laureate.motivation.isNotBlank()) {
            Text(
                text = laureate.motivation,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

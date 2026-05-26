package com.example.nobellaureatesclient.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nobellaureatesclient.domain.model.NobelCategory
import com.example.nobellaureatesclient.domain.model.NobelPrize
import com.example.nobellaureatesclient.presentation.common.UiState
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrizesListScreen(
    onPrizeClick: (NobelPrize) -> Unit,
    viewModel: PrizesListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Нобелевские лауреаты") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            FiltersRow(
                year = state.yearInput,
                category = state.selectedCategory,
                onYearChange = viewModel::onYearChange,
                onCategoryChange = viewModel::onCategoryChange,
                onApply = viewModel::applyFilters
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (val s = state.prizes) {
                    is UiState.Loading -> LoadingView()
                    is UiState.Error -> ErrorView(message = s.message, onRetry = viewModel::retry)
                    is UiState.Success -> PrizesList(prizes = s.data, onPrizeClick = onPrizeClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltersRow(
    year: String,
    category: NobelCategory,
    onYearChange: (String) -> Unit,
    onCategoryChange: (NobelCategory) -> Unit,
    onApply: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = year,
                onValueChange = onYearChange,
                label = { Text("Год") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(120.dp)
            )
            Spacer(Modifier.width(12.dp))

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = category.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Категория") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    NobelCategory.entries.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.displayName) },
                            onClick = {
                                onCategoryChange(item)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onApply, modifier = Modifier.align(Alignment.End)) {
            Text("Применить")
        }
    }
}

@Composable
private fun PrizesList(
    prizes: List<NobelPrize>,
    onPrizeClick: (NobelPrize) -> Unit
) {
    if (prizes.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Ничего не найдено", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(
            items = prizes,
            key = { index, prize ->
                "${index}_${prize.id}_${prize.awardYear}_${prize.category}"
            }
        ) { _, prize ->
            PrizeCard(prize = prize, onClick = { onPrizeClick(prize) })
        }
    }
}

@Composable
private fun PrizeCard(prize: NobelPrize, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${prize.awardYear} · ${prize.categoryFullName.ifBlank { prize.category.displayName }}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            val laureate = prize.laureates.firstOrNull()
            Text(
                text = laureate?.fullName ?: "Лауреат не указан",
                style = MaterialTheme.typography.bodyLarge
            )
            val motivation = laureate?.motivation.orEmpty()
            if (motivation.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                val preview = motivation.take(100).let { if (motivation.length > 100) "$it…" else it }
                Text(
                    text = preview,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (prize.laureates.size > 1) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "и ещё ${prize.laureates.size - 1}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Ошибка: $message",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRetry) { Text("Повторить") }
        }
    }
}

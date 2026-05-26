package com.example.nobellaureatesclient.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nobellaureatesclient.domain.model.NobelCategory
import com.example.nobellaureatesclient.domain.model.NobelPrize
import com.example.nobellaureatesclient.domain.usecase.GetNobelPrizesUseCase
import com.example.nobellaureatesclient.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PrizesListUiModel(
    val yearInput: String = "",
    val selectedCategory: NobelCategory = NobelCategory.ALL,
    val prizes: UiState<List<NobelPrize>> = UiState.Loading
)

@HiltViewModel
class PrizesListViewModel @Inject constructor(
    private val getNobelPrizes: GetNobelPrizesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PrizesListUiModel())
    val state: StateFlow<PrizesListUiModel> = _state.asStateFlow()

    init {
        load()
    }

    fun onYearChange(value: String) {
        if (value.length <= 4 && value.all { it.isDigit() }) {
            _state.update { it.copy(yearInput = value) }
        }
    }

    fun onCategoryChange(category: NobelCategory) {
        _state.update { it.copy(selectedCategory = category) }
        load()
    }

    fun applyFilters() = load()

    fun retry() = load()

    private fun load() {
        val current = _state.value
        val year = current.yearInput.toIntOrNull()
        _state.update { it.copy(prizes = UiState.Loading) }
        viewModelScope.launch {
            getNobelPrizes(year, current.selectedCategory)
                .onSuccess { prizes ->
                    _state.update { it.copy(prizes = UiState.Success(prizes)) }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(prizes = UiState.Error(throwable.message ?: "Неизвестная ошибка"))
                    }
                }
        }
    }
}

package com.example.nobellaureatesclient.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nobellaureatesclient.domain.model.NobelPrize
import com.example.nobellaureatesclient.domain.usecase.GetNobelPrizeDetailsUseCase
import com.example.nobellaureatesclient.presentation.common.UiState
import com.example.nobellaureatesclient.presentation.navigation.NobelDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrizeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetails: GetNobelPrizeDetailsUseCase
) : ViewModel() {

    private val year: String = savedStateHandle.get<String>(NobelDestinations.ARG_YEAR).orEmpty()
    private val categoryCode: String = savedStateHandle.get<String>(NobelDestinations.ARG_CATEGORY)
        ?.takeIf { it != "none" }
        .orEmpty()

    private val _state = MutableStateFlow<UiState<NobelPrize>>(UiState.Loading)
    val state: StateFlow<UiState<NobelPrize>> = _state.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            getDetails(year, categoryCode)
                .onSuccess { _state.value = UiState.Success(it) }
                .onFailure {
                    _state.value = UiState.Error(it.message ?: "Не удалось загрузить детали")
                }
        }
    }
}

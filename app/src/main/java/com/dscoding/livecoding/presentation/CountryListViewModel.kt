@file:OptIn(FlowPreview::class)

package com.dscoding.livecoding.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dscoding.livecoding.R
import com.dscoding.livecoding.domain.CountryRepository
import com.dscoding.livecoding.domain.util.Failure
import com.dscoding.livecoding.domain.util.Result
import com.dscoding.livecoding.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel @Inject constructor(private val countryRepository: CountryRepository) :
    ViewModel() {

    var state by mutableStateOf(CountryListState())
        private set

    private val eventChannel = Channel<CountryListEvent>()
    val events = eventChannel.receiveAsFlow()

    private val searchText = snapshotFlow { state.searchText }

    init {
        searchText
            .debounce(1000L)
            .filter { it?.isNotEmpty() == true }
            .onEach { query ->
                query?.let {
                    getCountries(query)
                }
            }.launchIn(viewModelScope)
    }


    fun onAction(action: CountryListAction) {
        when (action) {
            is CountryListAction.OnSearchTextEntered -> {
                state = state.copy(
                    searchText = action.searchText
                )
            }
        }
    }

    private suspend fun getCountries(searchText: String) {
        state = state.copy(isLoading = true)
        when (val result = countryRepository.getCountries(searchText)) {
            is Result.Error -> when (result.failure) {
                Failure.InternetConnection -> {
                    eventChannel.send(
                        CountryListEvent.OnLoadCountriesError(
                            UiText.StringResource(R.string.error_internet_connection)
                        )
                    )
                }

                Failure.ServerError -> {
                    eventChannel.send(
                        CountryListEvent.OnLoadCountriesError(
                            UiText.StringResource(R.string.error_server_error)
                        )
                    )
                }

                Failure.Unknown -> {
                    eventChannel.send(
                        CountryListEvent.OnLoadCountriesError(
                            UiText.StringResource(R.string.error_unknown)
                        )
                    )
                }
            }

            is Result.Success -> state = state.copy(countries = result.data)
        }
        state = state.copy(isLoading = false)
    }
}

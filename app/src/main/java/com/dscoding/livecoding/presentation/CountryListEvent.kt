package com.dscoding.livecoding.presentation

import com.dscoding.livecoding.presentation.util.UiText

sealed interface CountryListEvent {
    data class OnLoadCountriesError(val message: UiText) : CountryListEvent
}
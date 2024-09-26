package com.dscoding.livecoding.presentation

sealed interface CountryListAction {
    data class OnSearchTextEntered(val searchText: String) : CountryListAction
}
package com.dscoding.livecoding.presentation

import com.dscoding.livecoding.domain.Country

data class CountryListState(
    val isLoading: Boolean = false,
    val countries: List<Country> = emptyList(),
    val searchText: String? = null
)
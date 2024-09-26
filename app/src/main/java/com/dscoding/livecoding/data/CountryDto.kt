package com.dscoding.livecoding.data

data class CountryDto(
    val name: Name,
) {
    data class Name(
        val common: String,
    )
}

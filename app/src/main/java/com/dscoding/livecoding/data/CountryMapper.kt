package com.dscoding.livecoding.data

import com.dscoding.livecoding.domain.Country

fun CountryDto.toCountry() : Country {
    return Country(
        name = name.common
    )
}
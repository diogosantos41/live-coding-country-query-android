package com.dscoding.livecoding.domain

import com.dscoding.livecoding.domain.util.Failure
import com.dscoding.livecoding.domain.util.Result

interface CountryRepository {
    suspend fun getCountries(searchText: String): Result<List<Country>, Failure>
}
package com.dscoding.livecoding.data

import com.dscoding.livecoding.domain.Country
import com.dscoding.livecoding.domain.CountryRepository
import com.dscoding.livecoding.domain.util.Failure
import com.dscoding.livecoding.domain.util.Result
import kotlinx.coroutines.delay

class FakeCountryRepository : CountryRepository {

    private var shouldReturnUnknownError = false

    val countries = mutableListOf(
        CountryDto(name = CountryDto.Name("Germany")),
        CountryDto(name = CountryDto.Name("France")),
        CountryDto(name = CountryDto.Name("Portugal")),
        CountryDto(name = CountryDto.Name("Spain"))
    )

    override suspend fun getCountries(searchText: String): Result<List<Country>, Failure> {
        delay(100L)
        return when {
            shouldReturnUnknownError -> Result.Error(Failure.Unknown)
            countries.isEmpty() -> Result.Error(Failure.ServerError)
            else -> Result.Success(countries
                .filter {
                    it.name.common.contains(
                        searchText,
                        ignoreCase = true
                    )
                }
                .map { it.toCountry() })
        }
    }

    fun simulateUnknownError() {
        shouldReturnUnknownError = true
    }

    fun resetErrors() {
        shouldReturnUnknownError = false
    }
}
package com.dscoding.livecoding.data

import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {

    @GET("name/{query}")
    suspend fun getCountries(
        @Path("query") query: String
    ): List<CountryDto>

}


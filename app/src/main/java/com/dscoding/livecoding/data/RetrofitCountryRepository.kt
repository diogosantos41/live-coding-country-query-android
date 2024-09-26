package com.dscoding.livecoding.data

import com.dscoding.livecoding.domain.Country
import com.dscoding.livecoding.domain.CountryRepository
import com.dscoding.livecoding.domain.util.Failure
import com.dscoding.livecoding.domain.util.Result
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

class RetrofitCountryRepository(private val countryApi: CountryApi) : CountryRepository {
    override suspend fun getCountries(searchText: String): Result<List<Country>, Failure> {
        return try {
            val countryList = countryApi.getCountries(searchText).map { it.toCountry() }
            Result.Success(countryList)
        } catch (e: HttpException) {
            Result.Error(Failure.ServerError)
        } catch (e: UnknownHostException) {
            Result.Error(Failure.InternetConnection)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(Failure.Unknown)
        }
    }
}
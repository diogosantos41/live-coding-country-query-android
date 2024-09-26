package com.dscoding.livecoding.di

import com.dscoding.livecoding.BuildConfig
import com.dscoding.livecoding.data.CountryApi
import com.dscoding.livecoding.data.RetrofitCountryRepository
import com.dscoding.livecoding.domain.CountryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun providesCountryApi(): CountryApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryApi::class.java)

    @Provides
    @Singleton
    fun providesCountryRepository(countryApi: CountryApi): CountryRepository {
        return RetrofitCountryRepository(countryApi)
    }
}
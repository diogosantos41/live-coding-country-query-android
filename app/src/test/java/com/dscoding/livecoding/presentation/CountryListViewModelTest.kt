@file:OptIn(ExperimentalCoroutinesApi::class)

package com.dscoding.livecoding.presentation

import com.dscoding.livecoding.data.FakeCountryRepository
import com.dscoding.livecoding.domain.Country
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class CountryListViewModelTest {

    private lateinit var fakeCountryRepository: FakeCountryRepository
    private lateinit var viewModel: CountryListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeCountryRepository = FakeCountryRepository()
        viewModel = CountryListViewModel(fakeCountryRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        fakeCountryRepository.resetErrors()
    }

    @Test
    fun `initial state is correct`() {
        Truth.assertThat(viewModel.state.isLoading).isFalse()
        Truth.assertThat(viewModel.state.countries).isEmpty()
        Truth.assertThat(viewModel.state.searchText).isNull()
    }

    @Test
    fun `search countries updates state correctly`() = runTest {
        viewModel.onAction(CountryListAction.OnSearchTextEntered("Ger"))

        advanceTimeBy(1500L)

        Truth.assertThat(viewModel.state.countries.size).isEqualTo(1)
        Truth.assertThat(viewModel.state.countries).contains(Country("Germany"))
    }

    @Test
    fun `search countries with invalid name returns empty list`() = runTest {
        viewModel.onAction(CountryListAction.OnSearchTextEntered("ABC"))

        advanceTimeBy(1500L)

        Truth.assertThat(viewModel.state.countries.size).isEqualTo(0)
    }

    @Test
    fun `search countries with empty string doesn't load anything`() = runTest {
        viewModel.onAction(CountryListAction.OnSearchTextEntered("Ger"))

        advanceTimeBy(1500L)

        Truth.assertThat(viewModel.state.countries.size).isEqualTo(1)
        Truth.assertThat(viewModel.state.countries).contains(Country("Germany"))

        viewModel.onAction(CountryListAction.OnSearchTextEntered(""))

        advanceTimeBy(1500L)

        Truth.assertThat(viewModel.state.countries.size).isEqualTo(1)
        Truth.assertThat(viewModel.state.countries).contains(Country("Germany"))
    }

    @Test
    fun `no countries scenario`() = runTest {
        fakeCountryRepository.countries.clear()

        viewModel.onAction(CountryListAction.OnSearchTextEntered("Ger"))

        advanceTimeBy(1500L)

        Truth.assertThat(viewModel.state.countries.size).isEqualTo(0)
    }

    @Test
    fun `loading state is true when fetching countries`() = runTest {
        viewModel.onAction(CountryListAction.OnSearchTextEntered("Ger"))

        advanceTimeBy(1001L)

        Truth.assertThat(viewModel.state.isLoading).isTrue()

        advanceTimeBy(1500L)

        Truth.assertThat(viewModel.state.isLoading).isFalse()
    }

    @Test
    fun `debounce mechanism works correctly`() = runTest {
        viewModel.onAction(CountryListAction.OnSearchTextEntered("Po"))
        Truth.assertThat(viewModel.state.countries.size).isEqualTo(0)
        advanceTimeBy(500L)

        viewModel.onAction(CountryListAction.OnSearchTextEntered("Ger"))
        advanceTimeBy(1500L)

        Truth.assertThat(viewModel.state.countries.size).isEqualTo(1)
    }

    @Test
    fun `errors do not change state`() = runTest {
        fakeCountryRepository.simulateUnknownError()

        viewModel.onAction(CountryListAction.OnSearchTextEntered("Ger"))
        advanceTimeBy(1500L)

        Truth.assertThat(viewModel.state.countries.size).isEqualTo(0)
    }
}
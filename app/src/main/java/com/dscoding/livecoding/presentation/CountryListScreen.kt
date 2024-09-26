@file:OptIn(ExperimentalMaterial3Api::class)

package com.dscoding.livecoding.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dscoding.livecoding.presentation.ui.theme.LiveCodingTheme

@Composable
fun CountryListScreenRoot(viewModel: CountryListViewModel = hiltViewModel()) {
    val state = viewModel.state
    val onAction = viewModel::onAction

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is CountryListEvent.OnLoadCountriesError -> {
                    keyboardController?.hide()
                    Toast.makeText(
                        context,
                        event.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    CountryListScreen(state = state, onAction = onAction)
}


@Composable
fun CountryListScreen(state: CountryListState, onAction: (CountryListAction) -> Unit) {
    SearchBar(
        query = state.searchText ?: "",
        onQueryChange = { onAction(CountryListAction.OnSearchTextEntered(it)) },
        onSearch = {},
        active = true,
        onActiveChange = {},
        modifier = Modifier.fillMaxSize(),
        leadingIcon = {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(15.dp), strokeWidth = 2.dp)
            } else {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(state.countries) { country ->
                Text(
                    text = country.name,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun CountryListScreenPreview() {
    LiveCodingTheme {
        CountryListScreen(state = CountryListState(), onAction = {})
    }
}
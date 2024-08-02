package com.yeceylan.groupmaker.ui.sport_types

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.core.Response
import com.yeceylan.groupmaker.domain.model.SportTypeData
import com.yeceylan.groupmaker.domain.use_cases.sport_type.GetSportTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SportTypeViewModel @Inject constructor(
    private val sportTypeUseCase: GetSportTypeUseCase
) : ViewModel() {
    var booksResponse by mutableStateOf<Response<List<SportTypeData>>>(Response.Loading)

    init {
        getBooks()
    }

    private fun getBooks() = viewModelScope.launch {
        sportTypeUseCase.invoke().collect { response ->
            booksResponse = response
        }
    }
}
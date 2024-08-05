package com.yeceylan.groupmaker.ui.sport_types

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.SportTypeData
import com.yeceylan.groupmaker.domain.use_cases.sport_type.GetSportTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SportTypeViewModel @Inject constructor(
    private val sportTypeUseCase: GetSportTypeUseCase
) : ViewModel() {

    private var sportTypesResponse by mutableStateOf<Resource<List<SportTypeData>>>(Resource.Loading())

    private val _sportTypeList = MutableStateFlow<List<SportTypeData>>(emptyList())
    val sportTypeList: StateFlow<List<SportTypeData>> = _sportTypeList

    init {
        getSportTypes()
    }

    private fun getSportTypes() = viewModelScope.launch {
        sportTypeUseCase().collect {
            sportTypesResponse = it

            when (sportTypesResponse) {
                is Resource.Error -> "TODO()"
                is Resource.Loading -> "TODO()"
                is Resource.Success -> _sportTypeList.value = sportTypesResponse.data!!
            }
        }
    }
}
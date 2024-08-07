package com.yeceylan.groupmaker.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.use_cases.GetUserUseCase
import com.yeceylan.groupmaker.ui.auth.login.LoginViewModel.LoginScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCase: GetUserUseCase
) : ViewModel() {
    private var userResponse by mutableStateOf<Resource<User>>(Resource.Loading())

    private var _user = MutableStateFlow(User())
    var user = _user.asStateFlow()

    init {
        getProfile()
    }

    private fun getProfile() = viewModelScope.launch {
        useCase().collect {
            userResponse = it

            when (userResponse) {
                is Resource.Error -> "TODO()"
                is Resource.Loading -> "TODO()"
                is Resource.Success -> _user.value = it.data!!
            }
        }
    }

    private fun updateProfile() {

    }
}
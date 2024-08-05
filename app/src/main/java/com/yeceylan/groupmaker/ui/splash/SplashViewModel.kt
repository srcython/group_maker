package com.yeceylan.groupmaker.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.domain.use_cases.auth.IsLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isLoginUseCase: IsLoggedInUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        checkIsLoginState()
    }

    private fun checkIsLoginState() {
        isLoginUseCase().onEach { isLogin ->
            _uiState.update {
                it.copy(isLogin = isLogin)
            }
        }.launchIn(viewModelScope)
    }

    data class SplashScreenState(val isLogin: Boolean = false)
}
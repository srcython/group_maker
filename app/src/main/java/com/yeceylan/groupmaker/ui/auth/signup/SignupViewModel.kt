package com.yeceylan.groupmaker.ui.auth.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.use_cases.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun signUpWithEmailAndPassword(email: String, password: String) =
        registerUseCase(email, password).onEach {
            when (it) {
                is Resource.Loading -> {
                    _uiState.update { state ->
                        state.copy(isLoading = true)
                    }
                }

                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isHaveError = false,
                            isSuccessSignUpWithEmailAndPassword = true,
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isHaveError = true,
                            errorMessage = it.errorMessage.orEmpty(),
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)

    fun signUpWithGoogle() {
        // TODO : SignUpWithGoogle
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        val regex = "^[a-zA-Z0-9.*^$#@!%&_=+]*\$".toRegex()
        return password.matches(regex) && password.length >= 6
    }

    fun isPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    data class SignUpUIState(
        val isLoading: Boolean = false,
        val isHaveError: Boolean = false,
        val errorMessage: String = "",
        val isSuccessGoogleSignup: Boolean = false,
        val isSuccessSignUpWithEmailAndPassword: Boolean = false,
    )
}

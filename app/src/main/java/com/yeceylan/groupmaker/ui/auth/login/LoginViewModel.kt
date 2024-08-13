package com.yeceylan.groupmaker.ui.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.yeceylan.groupmaker.R
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.use_cases.auth.LoginUseCase
import com.yeceylan.groupmaker.domain.use_cases.auth.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun loginWithGoogle(account: GoogleSignInAccount) {
        signInWithGoogleUseCase(account).onEach { result ->
            handleAuthResult(result, isGoogleLogin = true)
        }.launchIn(viewModelScope)
    }

    fun loginWithEmailAndPassword(email: String, password: String, context: Context) {
        if (email.isEmpty() || password.isEmpty()) {
            updateUIStateWithError(context.getString(R.string.email_and_password_cannot_be_empty))
        } else if (password.length < 6) {
            updateUIStateWithError(context.getString(R.string.password_must_be_at_least_6_characters_long))
        } else {
            loginUseCase(email, password).onEach { result ->
                handleAuthResult(result, isGoogleLogin = false)
            }.launchIn(viewModelScope)
        }
    }

    private fun handleAuthResult(result: Resource<AuthResult>, isGoogleLogin: Boolean) {
        when (result) {
            is Resource.Loading -> _uiState.update { state ->
                state.copy(isLoading = true)
            }
            is Resource.Success -> _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    isSuccessGoogleLogin = isGoogleLogin,
                    isSuccessEmailAndPasswordLogin = !isGoogleLogin
                )
            }
            is Resource.Error -> _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    isHaveError = true,
                    errorMessage = result.errorMessage ?: ""
                )
            }
        }
    }

    private fun updateUIStateWithError(errorMessage: String) {
        _uiState.update {
            it.copy(
                isHaveError = true,
                errorMessage = errorMessage
            )
        }
    }

    fun updateErrorStatesWithDefaultValues() {
        _uiState.update {
            it.copy(
                isHaveError = false,
                errorMessage = ""
            )
        }
    }

    fun resetUIState() {
        _uiState.update {
            LoginScreenUIState()
        }
    }

    data class LoginScreenUIState(
        val isLoading: Boolean = false,
        val isHaveError: Boolean = false,
        val errorMessage: String = "",
        val isSuccessGoogleLogin: Boolean = false,
        val isSuccessEmailAndPasswordLogin: Boolean = false
    )
}

package com.yeceylan.groupmaker.ui.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.use_cases.AddStorageUseCase
import com.yeceylan.groupmaker.domain.use_cases.AddUserUseCase
import com.yeceylan.groupmaker.domain.use_cases.GetUserUseCase
import com.yeceylan.groupmaker.domain.use_cases.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCase: GetUserUseCase,
    private val updateUseCase: AddUserUseCase,
    private val addStorageUseCase: AddStorageUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    var userResponse by mutableStateOf<Resource<User>>(Resource.Loading())
        private set

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
                is Resource.Loading -> ""
                is Resource.Success -> _user.value = it.data!!
            }
        }
    }

    fun updateProfile(mUser: User, uri: Uri) = viewModelScope.launch {

        addStorageUseCase(uri, mUser.id).collect {

            _user.value.photoUrl = it.data!!
            updateUseCase(_user.value)
        }
    }

    fun logout()=viewModelScope.launch{
        logoutUseCase()
    }
}
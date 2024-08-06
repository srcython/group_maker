package com.yeceylan.groupmaker.ui.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.use_cases.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MakeMatchViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val users: StateFlow<Resource<List<User>>> = _users

    init {
        fetchUsers()
    }

    private fun fetchUsers() =
        viewModelScope.launch {
            getUsersUseCase().collect { resource ->
                _users.value = resource
            }
        }
}

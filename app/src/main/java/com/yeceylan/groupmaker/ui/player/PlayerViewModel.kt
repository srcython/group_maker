package com.yeceylan.groupmaker.ui.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.use_cases.GetUsersUseCase
import com.yeceylan.groupmaker.domain.use_cases.AddMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.AddUserUseCase
import com.yeceylan.groupmaker.domain.use_cases.UpdateMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.GetActiveMatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addMatchUseCase: AddMatchUseCase,
    private val updateMatchUseCase: UpdateMatchUseCase,
    private val getActiveMatchUseCase: GetActiveMatchUseCase,
    private val addUserUseCase: AddUserUseCase,
    ) : ViewModel() {

    private val _users = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val users: StateFlow<Resource<List<User>>> = _users

    private val _selectedUsers = MutableStateFlow<List<User>>(emptyList())
    val selectedUsers: StateFlow<List<User>> = _selectedUsers

    init {
        fetchUsers()
    }

    private fun fetchUsers() =
        viewModelScope.launch {
            getUsersUseCase().collect { resource ->
                _users.value = resource
            }
        }



    fun addUserToFirestore(user: User) {
         val userId = UUID.randomUUID().toString()
         val userWithId = user.copy(id = userId)
        viewModelScope.launch {
            addUserUseCase(userWithId)
        }
         addUser(userWithId)
    }
    fun updateSelectedUsers(updatedList: List<User>) {
        _selectedUsers.value = updatedList
        updateCurrentMatch()
    }

    fun addUser(user: User) {
        _selectedUsers.value = _selectedUsers.value + user
        updateCurrentMatch()
    }

    fun removeUser(user: User) {
        _selectedUsers.value = _selectedUsers.value - user
        updateCurrentMatch()
    }

    private fun updateCurrentMatch() {
        val auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid ?: run {
            Log.d("PlayerViewModel", "No user logged in")
            return
        }

        viewModelScope.launch {
            try {
                val activeMatch = getActiveMatchUseCase(currentUserId)
                if (activeMatch != null) {
                    // Update the existing match
                    val updatedPlayerList = activeMatch.playerList!!.toMutableList().apply {
                        clear()
                        addAll(_selectedUsers.value)
                    }
                    val updatedMatch = activeMatch.copy(playerList = updatedPlayerList)

                    updateMatchUseCase(currentUserId, updatedMatch)
                    Log.d("PlayerViewModel", "Active match updated with new players: ${updatedMatch.id}")
                } else {
                    // Create a new match
                    val newMatch = Match(
                        id = UUID.randomUUID().toString(),
                        matchLocationTitle = "",
                        matchLocation = "",
                        matchDate = "",
                        matchTime = "",
                        firstTeamName = "",
                        secondTeamName = "",
                        type = "",
                        maxPlayer = 0,
                        playerList = _selectedUsers.value,
                        firstTeamPlayerList = emptyList(),
                        secondTeamPlayerList = emptyList(),
                        result = null,
                        latLng = null,
                        isActive = true
                    )

                    addMatchUseCase(currentUserId, newMatch)
                    Log.d("PlayerViewModel", "New match created and added: ${newMatch.id}")
                }
            } catch (e: Exception) {
                Log.e("PlayerViewModel", "Error managing match: ${e.message}")
            }
        }
    }
}

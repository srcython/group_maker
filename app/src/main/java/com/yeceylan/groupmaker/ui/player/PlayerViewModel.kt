package com.yeceylan.groupmaker.ui.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.use_cases.GetUsersUseCase
import com.yeceylan.groupmaker.domain.use_cases.AddMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.UpdateMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.GetActiveMatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addMatchUseCase: AddMatchUseCase,
    private val updateMatchUseCase: UpdateMatchUseCase, // New use case
    private val getActiveMatchUseCase: GetActiveMatchUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _selectedUsers = MutableStateFlow<List<User>>(emptyList())
    val selectedUsers: StateFlow<List<User>> = _selectedUsers

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            try {
                val fetchedUsers = getUsersUseCase()
//                _users.value = fetchedUsers
            } catch (e: Exception) {
                // Handle error here
            }
        }
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
                        team1 = null,
                        team2 = null,
                        date = null,
                        location = null,
                        result = null,
                        type = "volleyball",
                        playerList = _selectedUsers.value,
                        macPlayer = null,
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

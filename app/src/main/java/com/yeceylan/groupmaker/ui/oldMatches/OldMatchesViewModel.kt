package com.yeceylan.groupmaker.ui.oldMatches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.domain.use_cases.GetAllMatchesUseCase
import com.yeceylan.groupmaker.domain.use_cases.auth.GetCurrentUserUidUseCase
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.user.User
import com.yeceylan.groupmaker.domain.use_cases.AddOldMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.AddUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OldMatchesViewModel @Inject constructor(
    private val getAllMatchesUseCase: GetAllMatchesUseCase,
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val addOldMatchUseCase: AddOldMatchUseCase,
) : ViewModel() {

    private val _oldMatches = MutableStateFlow<Resource<List<Match>>>(Resource.Loading())
    val oldMatches: StateFlow<Resource<List<Match>>> get() = _oldMatches

    init {
        fetchOldMatches()
    }

    private fun fetchOldMatches() {
        viewModelScope.launch {
            try {
                val currentUserUid = getCurrentUserUidUseCase()

                getAllMatchesUseCase().collect { matches ->
                    val filteredMatches = matches.filter { match ->
                        match.playerList.any { player -> player.id == currentUserUid }
                    }
                    _oldMatches.value = Resource.Success(filteredMatches)
                }
            } catch (e: Exception) {
                _oldMatches.value = Resource.Error("Eski maçlar yüklenirken bir hata oluştu: ${e.message}")
            }
        }
    }

    fun updateMatchResult(match: Match, result: String) {
        viewModelScope.launch {
            try {
                val updatedMatch = match.copy(result = result)
                addOldMatchUseCase(updatedMatch) // Update match in Firebase
                fetchOldMatches() // Refresh the list to show updated result
            } catch (e: Exception) {
                // Handle errors, such as logging or showing a user-friendly message
            }
        }
    }

    fun updatePlayerRating(matchId: String, playerId: String, rating: Int) {
        viewModelScope.launch {
            try {
                // Find the match and update the player's rating
                val match = _oldMatches.value.data?.find { it.id == matchId }
                val updatedPlayers = match?.playerList?.map { player ->
                    if (player.id == playerId) {
                        val calcRating = calculateRating(player, rating)
                        player.copy(
                            point = calcRating,
                            scoreCount = (player.scoreCount ?: 0) + 1
                        )
                    } else {
                        player
                    }
                } ?: emptyList()

                // Create an updated match with the new player list
                val updatedMatch = match?.copy(playerList = updatedPlayers)

                updatedMatch?.let {
                    // Update the match in Firebase
                    val currentUserUid = getCurrentUserUidUseCase() // Ensure to get the current user UID
                    //updateMatchInFirebase(currentUserUid, it) // Call a method to update match in Firestore

                    // Update the user information
                    val updatedPlayer = updatedPlayers.find { player -> player.id == playerId }
                    if (updatedPlayer != null) {
                        addUserUseCase(updatedPlayer) // Update player info
                    }

                    // Optionally refresh the old matches to reflect changes
                    fetchOldMatches()
                }
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    /*private suspend fun updateMatchInFirebase(userId: String, match: Match) {
        // Make sure to use the correct repository method to update the match
        userRepository.updateMatch(userId, match)
    }*/

    private fun calculateRating(player: User, rating: Int): Int {
        val currentPoints = player.point
        val currentCount = player.scoreCount ?: 0

        if (currentCount == 0) return rating

        return ((currentPoints * currentCount) + rating) / (currentCount + 1)
    }


}



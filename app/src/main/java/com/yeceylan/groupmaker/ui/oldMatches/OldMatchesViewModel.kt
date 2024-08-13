package com.yeceylan.groupmaker.ui.oldMatches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.domain.use_cases.user.GetAllMatchesUseCase
import com.yeceylan.groupmaker.domain.use_cases.auth.GetCurrentUserUidUseCase
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.user.User
import com.yeceylan.groupmaker.domain.use_cases.match.AddOldMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.user.AddUserUseCase
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
                addOldMatchUseCase(updatedMatch)
                fetchOldMatches()
            } catch (e: Exception) {
                // Handle errors, such as logging or showing a user-friendly message
            }
        }
    }

    fun updatePlayerRating(matchId: String, playerId: String, rating: Int) {
        viewModelScope.launch {
            try {
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

                val updatedMatch = match?.copy(playerList = updatedPlayers)

                updatedMatch?.let {

                    val updatedPlayer = updatedPlayers.find { player -> player.id == playerId }
                    if (updatedPlayer != null) {
                        addUserUseCase(updatedPlayer)
                    }

                    fetchOldMatches()
                }
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    private fun calculateRating(player: User, rating: Int): Int {
        val currentPoints = player.point
        val currentCount = player.scoreCount ?: 0

        if (currentCount == 0) return rating

        return ((currentPoints * currentCount) + rating) / (currentCount + 1)
    }
}



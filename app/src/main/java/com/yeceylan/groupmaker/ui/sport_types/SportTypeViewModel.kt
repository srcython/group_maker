package com.yeceylan.groupmaker.ui.sport_types

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.SportTypeData
import com.yeceylan.groupmaker.domain.use_cases.AddMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.GetActiveMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.UpdateMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.sport_type.GetSportTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SportTypeViewModel @Inject constructor(
    private val sportTypeUseCase: GetSportTypeUseCase,
    private val addMatchUseCase: AddMatchUseCase,
    private val getActiveMatchUseCase: GetActiveMatchUseCase,
    private val updateMatchUseCase: UpdateMatchUseCase,
    ) : ViewModel() {

    private var sportTypesResponse by mutableStateOf<Resource<List<SportTypeData>>>(Resource.Loading())

    private val _sportTypeList = MutableStateFlow<List<SportTypeData>>(emptyList())
    val sportTypeList: StateFlow<List<SportTypeData>> = _sportTypeList

    init {
        getSportTypes()
    }

    private fun getSportTypes() = viewModelScope.launch {
        sportTypeUseCase().collect {
            sportTypesResponse = it

            when (sportTypesResponse) {
                is Resource.Error -> {
                    // Log error
                    Log.e("SportTypeViewModel", "Error fetching sport types: ${(sportTypesResponse as Resource.Error).message}")
                }
                is Resource.Loading -> {
                    // Log loading state
                    Log.d("SportTypeViewModel", "Loading sport types...")
                }
                is Resource.Success -> {
                    _sportTypeList.value = sportTypesResponse.data!!
                    // Log success
                    Log.d("SportTypeViewModel", "Successfully fetched sport types")
                }
            }
        }
    }

    fun addMatch(title: String, teamSize: Int) {
        val auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid ?: run {
            Log.d("SportTypeViewModel", "No user logged in")
            return
        }

        viewModelScope.launch {
            try {
                val activeMatch = getActiveMatchUseCase(currentUserId)
                if (activeMatch != null) {
                    val updatedMatch = activeMatch.copy(type = title, maxPlayer = teamSize)

                    updateMatchUseCase(currentUserId, updatedMatch)
                    Log.d("PlayerViewModel", "Active match updated with new players: ${updatedMatch.id}")

                } else {
                    val newMatch = Match(
                        id = UUID.randomUUID().toString(),
                        team1 = null,
                        team2 = null,
                        date = null,
                        location = null,
                        result = null,
                        type = title,
                        playerList = emptyList(),
                        playerList1 = emptyList(),
                        maxPlayer = teamSize,
                    )

                    addMatchUseCase(currentUserId, newMatch)
                    Log.d("SportTypeViewModel", "New match created and added: ${newMatch.id}")
                }
            } catch (e: Exception) {
                Log.e("SportTypeViewModel", "Error adding match", e)
            }
        }
    }
}

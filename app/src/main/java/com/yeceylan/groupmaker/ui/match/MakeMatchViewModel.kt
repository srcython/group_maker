package com.yeceylan.groupmaker.ui.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.use_cases.GetActiveMatchUseCase
import com.yeceylan.groupmaker.domain.use_cases.UpdateMatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class MakeMatchViewModel @Inject constructor(
    private val getActiveMatchUseCase: GetActiveMatchUseCase,
    private val updateMatchUseCase: UpdateMatchUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _users = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val users: StateFlow<Resource<List<User>>> = _users

    private val _activeMatch = MutableStateFlow<Match?>(null)
    val activeMatch: StateFlow<Match?> = _activeMatch

    private val _team1Name = MutableStateFlow("Takım 1")
    val team1Name: StateFlow<String> = _team1Name

    private val _team2Name = MutableStateFlow("Takım 2")
    val team2Name: StateFlow<String> = _team2Name

    private val _selectedPersons1 = MutableStateFlow<List<User>>(emptyList())
    val selectedPersons1: StateFlow<List<User>> = _selectedPersons1

    private val _selectedPersons2 = MutableStateFlow<List<User>>(emptyList())
    val selectedPersons2: StateFlow<List<User>> = _selectedPersons2

    private val _matchLocation = MutableStateFlow("")
    val matchLocation: StateFlow<String> = _matchLocation

    private val _matchDate = MutableStateFlow("")
    val matchDate: StateFlow<String> = _matchDate

    private val _matchTime = MutableStateFlow("")
    val matchTime: StateFlow<String> = _matchTime

    private val _showPlayerCountDialog = MutableStateFlow(false)
    val showPlayerCountDialog: StateFlow<Boolean> = _showPlayerCountDialog

    private val _showChangeTeamNamesDialog = MutableStateFlow(false)
    val showChangeTeamNamesDialog: StateFlow<Boolean> = _showChangeTeamNamesDialog

    private val currentUserId = firebaseAuth.currentUser?.uid

    init {
        fetchActiveMatch()
    }

    private fun fetchActiveMatch() {
        viewModelScope.launch {
            val match = currentUserId?.let { getActiveMatchUseCase(it) }
            _activeMatch.value = match
            _users.value = match?.playerList?.let { Resource.Success(it) } ?: Resource.Success(emptyList())
        }
    }

    fun updateMatch(updatedMatchData: Match) {
        viewModelScope.launch {
            currentUserId?.let { userId ->
                val currentMatch = activeMatch.value
                if (currentMatch != null) {
                    val updatedMatch = currentMatch.copy(
                        type = updatedMatchData.type,
                        matchLocationTitle = updatedMatchData.matchLocationTitle,
                        matchLocation = updatedMatchData.matchLocation,
                        matchDate = updatedMatchData.matchDate,
                        matchTime = updatedMatchData.matchTime,
                        firstTeamName = updatedMatchData.firstTeamName,
                        secondTeamName = updatedMatchData.secondTeamName,
                        playerList = updatedMatchData.playerList,
                        firstTeamPlayerList = updatedMatchData.firstTeamPlayerList,
                        secondTeamPlayerList = updatedMatchData.secondTeamPlayerList,
                        isActive = updatedMatchData.isActive
                    )
                    updateMatchUseCase(userId, updatedMatch)
                }
            }
        }
    }

    fun updateMatchAndNavigate(
        navController: NavController,
        locationLatLng: LatLng?,
        selectedAddress: String
    ) {
        val updatedMatch = activeMatch.value?.copy(
            matchLocationTitle = _matchLocation.value,
            matchLocation = selectedAddress,
            matchDate = _matchDate.value,
            matchTime = _matchTime.value,
            firstTeamName = _team1Name.value,
            secondTeamName = _team2Name.value,
            firstTeamPlayerList = _selectedPersons1.value,
            secondTeamPlayerList = _selectedPersons2.value,
            latLng = locationLatLng,
            isActive = true
        )

        updatedMatch?.let {
            updateMatch(it)

            val matchInfoJson = Gson().toJson(it)
            val encodedJson = URLEncoder.encode(
                matchInfoJson,
                StandardCharsets.UTF_8.toString()
            ).replace("+", "%20")
            navController.navigate("matchInfo/$encodedJson")
        }
    }

    fun setTeam1Name(name: String) {
        _team1Name.value = name
    }

    fun setTeam2Name(name: String) {
        _team2Name.value = name
    }

    fun setMatchLocation(location: String) {
        _matchLocation.value = location
    }

    fun setMatchDate(date: String) {
        _matchDate.value = date
    }

    fun setMatchTime(time: String) {
        _matchTime.value = time
    }

    fun togglePlayerCountDialog(show: Boolean) {
        _showPlayerCountDialog.value = show
    }

    fun toggleChangeTeamNamesDialog(show: Boolean) {
        _showChangeTeamNamesDialog.value = show
    }

    fun setSelectedPersons1(users: List<User>) {
        _selectedPersons1.value = users
    }

    fun setSelectedPersons2(users: List<User>) {
        _selectedPersons2.value = users
    }
}

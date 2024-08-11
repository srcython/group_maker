package com.yeceylan.groupmaker.ui.oldMatches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.use_cases.GetAllMatchesUseCase
import com.yeceylan.groupmaker.domain.use_cases.auth.GetCurrentUserUidUseCase
import com.yeceylan.groupmaker.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OldMatchesViewModel @Inject constructor(
    private val getAllMatchesUseCase: GetAllMatchesUseCase,
    private val getCurrentUserUidUseCase: GetCurrentUserUidUseCase
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
}


package com.yeceylan.groupmaker.domain.repository

import com.yeceylan.groupmaker.domain.model.Match
import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    suspend fun addMatch(match: Match)
    fun getMatch(id: String): Flow<Match?>

    fun getAllMatches(): Flow<List<Match>>
}
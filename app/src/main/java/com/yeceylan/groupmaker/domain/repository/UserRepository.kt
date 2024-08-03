package com.yeceylan.groupmaker.domain.repository

import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.User

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun getUsers(): List<User>
    suspend fun addMatch(userId: String, match: Match)
    suspend fun updateMatch(userId: String, match: Match) // New method
    suspend fun getActiveMatch(userId: String): Match?
}

package com.yeceylan.groupmaker.domain.repository

import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun getUsers(): List<User>
    suspend fun getUser(): Flow<Resource<User>>
    suspend fun addMatch(userId: String, match: Match)
    suspend fun updateMatch(userId: String, match: Match) // New method
    suspend fun getActiveMatch(userId: String): Match?
    suspend fun updateUser(user: User):Flow<Resource<User>>
}

package com.yeceylan.groupmaker.domain.repository

import android.net.Uri
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.domain.model.user.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun getUsers(): Flow<Resource<List<User>>>
    suspend fun getUser(): Flow<Resource<User>>
    suspend fun addMatch(userId: String, match: Match)
    suspend fun updateMatch(userId: String, match: Match) // New method
    suspend fun getActiveMatch(userId: String): Match?
    suspend fun addStorage(uri: Uri,userId: String): Flow<Resource<String>>
}

package com.yeceylan.groupmaker.domain.repository

import com.yeceylan.groupmaker.domain.model.User

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun getUsers(): List<User>
}

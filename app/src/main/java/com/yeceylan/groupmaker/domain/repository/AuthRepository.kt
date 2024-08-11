package com.yeceylan.groupmaker.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.yeceylan.groupmaker.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun login(email: String, password: String): Flow<Resource<AuthResult>>

    fun register(email: String, password: String): Flow<Resource<AuthResult>>

    fun resetPassword(email: String): Flow<Resource<Void?>>

    suspend fun logout()

    suspend fun userUid(): String

    suspend fun isLoggedIn(): Boolean

    fun signInWithGoogle(account: GoogleSignInAccount): Flow<Resource<AuthResult>>
}

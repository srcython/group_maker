package com.yeceylan.groupmaker.domain.use_cases.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(account: GoogleSignInAccount): Flow<Resource<AuthResult>> {
        return repository.signInWithGoogle(account)
    }
}

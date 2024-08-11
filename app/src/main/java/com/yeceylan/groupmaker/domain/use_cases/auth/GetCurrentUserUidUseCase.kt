package com.yeceylan.groupmaker.domain.use_cases.auth

import com.yeceylan.groupmaker.domain.repository.AuthenticationRepository
import javax.inject.Inject

class GetCurrentUserUidUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(): String {
        return authenticationRepository.userUid()
    }
}

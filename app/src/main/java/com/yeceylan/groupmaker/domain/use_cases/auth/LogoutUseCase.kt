package com.yeceylan.groupmaker.domain.use_cases.auth

import com.yeceylan.groupmaker.domain.repository.AuthenticationRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {

    suspend operator fun invoke() = authenticationRepository.logout()
}

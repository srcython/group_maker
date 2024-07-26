package com.yeceylan.groupmaker.domain.use_cases.auth

import com.yeceylan.groupmaker.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {

    operator fun invoke() = flow { emit(authenticationRepository.isLoggedIn()) }
}

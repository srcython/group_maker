package com.yeceylan.groupmaker.domain.use_cases.auth

import com.yeceylan.groupmaker.domain.repository.AuthenticationRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String) = authenticationRepository.register(email, password)
}

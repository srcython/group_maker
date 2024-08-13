package com.yeceylan.groupmaker.domain.use_cases.user


import com.yeceylan.groupmaker.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getUser()
}
package com.yeceylan.groupmaker.domain.use_cases

import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) = userRepository.updateUser(user)
}
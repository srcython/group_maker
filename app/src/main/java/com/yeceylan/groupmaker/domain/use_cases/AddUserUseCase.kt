package com.yeceylan.groupmaker.domain.use_cases

import com.yeceylan.groupmaker.domain.model.user.User
import com.yeceylan.groupmaker.domain.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        userRepository.addUser(user)
    }
}

package com.yeceylan.groupmaker.domain.use_cases

import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.repository.UserRepository
import javax.inject.Inject

class GetActiveMatchUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Match? {
        return userRepository.getActiveMatch(userId)
    }
}

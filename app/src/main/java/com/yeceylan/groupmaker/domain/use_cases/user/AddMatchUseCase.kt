package com.yeceylan.groupmaker.domain.use_cases.user

import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.domain.repository.UserRepository
import javax.inject.Inject

class AddMatchUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, match: Match) {
        userRepository.addMatch(userId, match)
    }
}

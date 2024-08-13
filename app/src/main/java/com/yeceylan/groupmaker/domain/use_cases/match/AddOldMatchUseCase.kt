package com.yeceylan.groupmaker.domain.use_cases.match

import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.domain.repository.MatchRepository
import javax.inject.Inject

class AddOldMatchUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    suspend operator fun invoke(match: Match) = repository.addMatch(match)
}
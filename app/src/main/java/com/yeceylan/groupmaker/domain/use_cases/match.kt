package com.yeceylan.groupmaker.domain.use_cases

import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.repository.MatchRepository
import javax.inject.Inject

class AddOldMatchUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    suspend operator fun invoke(match: Match) = repository.addMatch(match)
}

class GetOldMatchUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    operator fun invoke(id: String) = repository.getMatch(id)
}

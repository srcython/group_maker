package com.yeceylan.groupmaker.domain.use_cases

import com.yeceylan.groupmaker.domain.repository.MatchRepository
import javax.inject.Inject

class GetOldMatchUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    operator fun invoke(id: String) = repository.getMatch(id)
}

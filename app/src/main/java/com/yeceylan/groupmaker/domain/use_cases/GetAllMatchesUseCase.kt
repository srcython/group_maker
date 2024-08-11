package com.yeceylan.groupmaker.domain.use_cases

import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMatchesUseCase @Inject constructor(
    private val repository: MatchRepository
) {
    operator fun invoke(): Flow<List<Match>> = repository.getAllMatches()
}

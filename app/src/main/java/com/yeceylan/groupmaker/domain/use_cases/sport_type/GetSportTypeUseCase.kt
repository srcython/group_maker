package com.yeceylan.groupmaker.domain.use_cases.sport_type

import com.yeceylan.groupmaker.domain.repository.SportTypeRepository

class GetSportTypeUseCase(private val repo: SportTypeRepository) {

    operator fun invoke() = repo.getSportTypeFromFirestore()

}
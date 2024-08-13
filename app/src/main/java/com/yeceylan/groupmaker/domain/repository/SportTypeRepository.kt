package com.yeceylan.groupmaker.domain.repository

import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.SportTypeData
import kotlinx.coroutines.flow.Flow

interface SportTypeRepository {

    fun getSportTypeFromFirestore(): Flow<Resource<List<SportTypeData>>>

}
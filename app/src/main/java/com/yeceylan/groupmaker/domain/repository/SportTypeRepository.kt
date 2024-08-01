package com.yeceylan.groupmaker.domain.repository

import com.yeceylan.groupmaker.core.Response
import com.yeceylan.groupmaker.domain.model.SportTypeData
import kotlinx.coroutines.flow.Flow

interface SportTypeRepository {

    fun getSportTypeFromFirestore(): Flow<Response<List<SportTypeData>>>

}
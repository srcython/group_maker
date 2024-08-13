package com.yeceylan.groupmaker.domain.use_cases.match

import android.net.Uri
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddStorageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uri: Uri, userId: String): Flow<Resource<String>> {
       return userRepository.addStorage(uri,userId)
    }
}
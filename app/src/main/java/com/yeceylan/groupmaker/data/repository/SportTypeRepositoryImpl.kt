package com.yeceylan.groupmaker.data.repository

import com.google.firebase.firestore.CollectionReference
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.SportTypeData
import com.yeceylan.groupmaker.domain.repository.SportTypeRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SportTypeRepositoryImpl @Inject constructor(
    private val sportTypeRef: CollectionReference
) : SportTypeRepository {

    override fun getSportTypeFromFirestore() = callbackFlow {

        val snapshotListener = sportTypeRef.addSnapshotListener { snapshot, e ->

            val sportTypeResponse = if (snapshot != null) {

                val sportTypes = snapshot.toObjects(SportTypeData::class.java)
                Resource.Success(sportTypes)

            } else {
                Resource.Error(e.toString())
            }
            trySend(sportTypeResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }


}
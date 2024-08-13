package com.yeceylan.groupmaker.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yeceylan.groupmaker.domain.model.match.Match
import com.yeceylan.groupmaker.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MatchRepositoryImpl(private val firestore: FirebaseFirestore) : MatchRepository {

    override suspend fun addMatch(match: Match) {
        firestore.collection("matches")
            .document(match.id)
            .set(match)
            .await()
    }

    override fun getMatch(id: String): Flow<Match?> = flow {
        val document = firestore.collection("matches")
            .document(id)
            .get()
            .await()
        val match = document.toObject(Match::class.java)
        emit(match)
    }

    override fun getAllMatches(): Flow<List<Match>> = flow {
        val documents = firestore.collection("matches")
            .get()
            .await()

        val matches = documents.mapNotNull { document ->
            document.toObject(Match::class.java)
        }
        emit(matches)
    }
}

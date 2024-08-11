package com.yeceylan.groupmaker.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.yeceylan.groupmaker.core.Resource
import com.yeceylan.groupmaker.domain.model.Match
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun addUser(user: User) {
        firestore.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }

    override suspend fun getUser() = callbackFlow {
        val snapshotListener = firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .addSnapshotListener{ snapshot, e ->

            val booksResponse = if (snapshot != null) {
                val books = snapshot.toObject<User>()!!
                Resource.Success(books)
            } else {
                Resource.Error(e.toString())
            }
            trySend(booksResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun getUsers(): Flow<Resource<List<User>>> = callbackFlow {
        trySend(Resource.Loading())
        val snapshotListener: ListenerRegistration = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->

                val usersResponse = if (snapshot != null) {
                    val users = snapshot.toObjects(User::class.java)
                    Resource.Success(users)
                } else {
                    Resource.Error(error?.message ?: "Veri alınamadı")
                }
                trySend(usersResponse)
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun updateMatch(userId: String, match: Match) {
        val userDocument = firestore.collection("users").document(userId)
        val matchesCollection = userDocument.collection("matches")

        matchesCollection.document(match.id)
            .update(
                "matchLocationTitle", match.matchLocationTitle,
                "matchLocation", match.matchLocation,
                "matchDate", match.matchDate,
                "firstTeamName", match.firstTeamName,
                "matchTime", match.matchTime,
                "latLng", match.latLng,
                "secondTeamName", match.secondTeamName,
                "type", match.type,
                "playerList", match.playerList,
                "firstTeamPlayerList", match.firstTeamPlayerList,
                "secondTeamPlayerList", match.secondTeamPlayerList
            )
            .await()
    }


    override suspend fun addMatch(userId: String, match: Match) {
        val userDocument = firestore.collection("users").document(userId)
        val matchesCollection = userDocument.collection("matches")
        matchesCollection.document(match.id)
            .set(match)
            .await()
    }

    override suspend fun getActiveMatch (userId: String): Match? {
        val userDocument = firestore.collection("users").document(userId)
        Log.d("UserRepositoryImpl", "Fetching active match for user: $userId")
        return try {
            // Log all matches for debugging
            logMatches(userId)

            val matchesSnapshot = userDocument.collection("matches")
                .whereEqualTo("active", true)
                .get()
                .await()

            Log.d("UserRepositoryImpl", "Matches snapshot size: ${matchesSnapshot.size()}")

            if (matchesSnapshot.documents.isNotEmpty()) {
                val match = matchesSnapshot.documents.firstOrNull()?.toObject(Match::class.java)
                Log.d("UserRepositoryImpl", "Active match found: $match")
                match
            } else {
                Log.d("UserRepositoryImpl", "No active match found for user $userId")
                null
            }
        } catch (e: Exception) {
            Log.e(
                "UserRepositoryImpl",
                "Error getting active match for user $userId: ${e.message}",
                e
            )
            null
        }
    }

     private suspend fun logMatches(userId: String) {
            val userDocument = firestore.collection("users").document(userId)
            val matchesSnapshot = userDocument.collection("matches")
                .get()
                .await()

            for (document in matchesSnapshot.documents) {
                Log.d("UserRepositoryImpl", "Match Document: ${document.id}, Data: ${document.data}")
            }
        }

}

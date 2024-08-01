package com.yeceylan.groupmaker.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yeceylan.groupmaker.domain.model.User
import com.yeceylan.groupmaker.domain.repository.UserRepository
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

    override suspend fun getUsers(): List<User> {
        return firestore.collection("users")
            .get()
            .await()
            .toObjects(User::class.java)
    }
}

package com.yeceylan.groupmaker.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val firstName: String = "",
    val userName: String = "",
    val surname: String? = "",
    val photoUrl: String? = null,
    val position: String? = "",
    val point: Int? = 0,
) {
    constructor() : this(
        id = "",
        firstName = "",
        email = "",
        userName = "",
        surname = "",
        photoUrl = "",
        position = "",
        point = 0,
    )
}


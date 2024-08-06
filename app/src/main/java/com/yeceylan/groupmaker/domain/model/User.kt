package com.yeceylan.groupmaker.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val surname: String? = "",
    val photoUrl: String? = "",
    val position: String? = "",
    val point: Int? = 0,
    val currentMatch:Match? = null,
) {
    constructor() : this(
       id = "",
        email = "",
        name = "",
        surname = "",
        photoUrl = "",
        position = "",
        point = 0,
        currentMatch = null
    )
}

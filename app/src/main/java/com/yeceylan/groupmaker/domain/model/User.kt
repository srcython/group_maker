package com.yeceylan.groupmaker.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val surname: String? = "",
    val photoResId: Int? = 0,
    val position: String? = "",
    val point: Int? = 0,
    val currentMatch:Match? = null,
) {
    constructor() : this(
       id = "",
        email = "",
        name = "",
        surname = "",
        photoResId = 0,
        position = "",
        point = 0,
        currentMatch = null
    )
}

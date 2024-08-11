package com.yeceylan.groupmaker.domain.model

data class User(
    val id: String = "",
    val email: String = "",
    var firstName: String = "",
    var userName: String = "",
    var surname: String = "",
    var photoUrl: String? = "",
    var position: String = "",
    val point: Int = 0,
    var iban: String = "",
    var scoreCount: Int? = 0,
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
        iban = "",
    )
}


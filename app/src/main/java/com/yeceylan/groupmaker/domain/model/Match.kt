package com.yeceylan.groupmaker.domain.model

data class Match(
    val id: String = "",
    val team1: String? = null,
    val team2: String? = null,
    val date: String? = null,
    val location: String? = null,
    val result: String? = null,
    val type: String = "volleyball",
    val playerList: List<User> = emptyList(),
    val maxPlayer: String? = null,
    val isActive: Boolean = true
) {
    constructor() : this(
        id = "",
        team1 = null,
        team2 = null,
        date = null,
        location = null,
        result = null,
        type = "volleyball",
        playerList = emptyList(),
        maxPlayer = null,
        isActive = true
    )
}

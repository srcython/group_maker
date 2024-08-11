package com.yeceylan.groupmaker.domain.model

data class Match(
    val id: String = "",
    val team1: String? = null,
    val team2: String? = null,
    val date: String? = null,
    val location: String? = null,
    val result: String? = null,
    val type: String? = null,
    val playerList: List<User> = emptyList(),
    val playerList1: List<User> = emptyList(),
    val maxPlayer: Int? = null,
    val isActive:Boolean = true,
) {
    constructor() : this(
        id = "",
        team1 = null,
        team2 = null,
        date = null,
        location = null,
        result = null,
        type = null,
        playerList = emptyList(),
        playerList1 = emptyList(),
        maxPlayer = null,
        isActive = true,
    )

}

package com.yeceylan.groupmaker.domain.model.match

import com.google.android.gms.maps.model.LatLng
import com.yeceylan.groupmaker.domain.model.user.User
import java.io.Serializable

data class Match(
    val id: String = "",
    val matchLocationTitle: String? = null,
    val matchLocation: String? = null,
    val matchDate: String? = null,
    val matchTime: String? = null,
    val firstTeamName: String? = null,
    val secondTeamName: String? = null,
    val type: String = "",
    val playerList: List<User> = emptyList(),
    val firstTeamPlayerList: List<User> = emptyList(),
    val secondTeamPlayerList: List<User> = emptyList(),
    val result: String? = null,
    val latLng: LatLng?,
    val isActive: Boolean = true,
    val maxPlayer : Int = 0,

    ):Serializable {
    constructor() : this(
        id = "",
        matchLocationTitle = null,
        matchLocation = null,
        matchDate = null,
        matchTime = null,
        firstTeamName = null,
        secondTeamName = null,
        type = "",
        playerList = emptyList(),
        firstTeamPlayerList = emptyList(),
        secondTeamPlayerList = emptyList(),
        result = null,
        latLng = null,
        isActive = true,
        maxPlayer = 0,
    )

}

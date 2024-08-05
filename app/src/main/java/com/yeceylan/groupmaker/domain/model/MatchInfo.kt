package com.yeceylan.groupmaker.domain.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class MatchInfo(
    val team1Name: String,
    val team2Name: String,
    val matchLocation: String,
    val matchDate: String,
    val matchTime: String,
    val latLng: LatLng?,
    val address: String
) : Serializable

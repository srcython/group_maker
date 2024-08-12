package com.yeceylan.groupmaker.ui.player.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class PlayerScreens {

    @Serializable
    data class PlayerPage(val title: String, val size: Int) : PlayerScreens()
}
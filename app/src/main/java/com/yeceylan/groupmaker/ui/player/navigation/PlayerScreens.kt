package com.yeceylan.groupmaker.ui.player.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class PlayerScreens {

    @Serializable
    data object PlayerPage : PlayerScreens()
}
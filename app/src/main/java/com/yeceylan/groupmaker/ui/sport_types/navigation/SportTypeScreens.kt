package com.yeceylan.groupmaker.ui.sport_types.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SportTypeScreens {

    @Serializable
    data object SportTypes : SportTypeScreens()

    @Serializable
    data object SportTypeSetting : SportTypeScreens()
}
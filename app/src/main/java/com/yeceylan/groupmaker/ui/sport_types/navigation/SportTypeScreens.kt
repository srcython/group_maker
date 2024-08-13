package com.yeceylan.groupmaker.ui.sport_types.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SportTypeScreens(
    //val route: String,
   // val pass:String,

    ) {
    @Serializable
    data class SportTypeSetting(val title: String, val teamSize: Int) : SportTypeScreens(
        // route = "sportTypeSetting/{title}/{size}",
        //pass = "sportTypeSetting"
    )
}
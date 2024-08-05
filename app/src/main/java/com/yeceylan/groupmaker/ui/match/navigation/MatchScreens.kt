package com.yeceylan.groupmaker.ui.match.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class MatchScreens(
) {

    @Serializable
    data class MakeMatchScreen(

        val route: String = "makeMatch/{teamSize}",
        val pass:String = "makeMatch",
    ) : MatchScreens()
}
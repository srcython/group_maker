package com.yeceylan.groupmaker.ui.bottombar

import com.yeceylan.groupmaker.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
) {

    @Serializable
    data object Home: BottomBarScreen(
        route = "home",
        title = "Ana Sayfa",
        icon = R.drawable.baseline_home_24,
        icon_focused = R.drawable.baseline_home_24
    )

    @Serializable
    data object History: BottomBarScreen(
        route = "history",
        title = "Maçlar",
        icon = R.drawable.baseline_history_24,
        icon_focused = R.drawable.baseline_history_24
    )

    @Serializable
    data object Profile: BottomBarScreen(
        route = "profile",
        title = "Profil",
        icon = R.drawable.baseline_person_24,
        icon_focused = R.drawable.baseline_person_24
    )

}
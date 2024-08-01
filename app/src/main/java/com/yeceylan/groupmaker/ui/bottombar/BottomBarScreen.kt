package com.yeceylan.groupmaker.ui.bottombar

import com.yeceylan.groupmaker.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
)  {

    data object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = R.drawable.baseline_home_24,
        icon_focused = R.drawable.baseline_home_24
    )

    data object History : BottomBarScreen(
        route = "history",
        title = "History",
        icon = R.drawable.baseline_history_24,
        icon_focused = R.drawable.baseline_history_24
    )

    data object Profile : BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = R.drawable.baseline_person_24,
        icon_focused = R.drawable.baseline_person_24
    )

}
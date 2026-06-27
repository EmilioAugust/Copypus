package com.emilioaugust.copypus.ui.screens

sealed class Screen(val route: String) {
    object History : Screen("history")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
}
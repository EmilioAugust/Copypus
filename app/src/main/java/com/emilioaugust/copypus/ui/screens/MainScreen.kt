package com.emilioaugust.copypus.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.emilioaugust.copypus.R
import com.emilioaugust.copypus.data.MainViewModel
import com.emilioaugust.copypus.data.SettingsViewModel
import com.emilioaugust.copypus.ui.ManualEntrySheet
import com.emilioaugust.copypus.ui.MultiPasteSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel, settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            Column {
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 0.8.dp
                )
                NavigationBar(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
                    val currentRoute =
                        navController.currentBackStackEntryAsState().value?.destination?.route
                    NavigationBarItem(
                        selected = currentRoute == Screen.History.route,
                        onClick = {
                            navController.navigate(Screen.History.route) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        label = { Text(stringResource(R.string.history_nav)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onTertiary,
                            unselectedTextColor = MaterialTheme.colorScheme.onTertiary,
                            indicatorColor = Color.Transparent
                        )
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Favorites.route,
                        onClick = {
                            navController.navigate(Screen.Favorites.route) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text(stringResource(R.string.favorites_nav)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onTertiary,
                            unselectedTextColor = MaterialTheme.colorScheme.onTertiary,
                            indicatorColor = Color.Transparent
                        )
                    )

                    NavigationBarItem(
                        selected = currentRoute == Screen.Settings.route,
                        onClick = {
                            navController.navigate(Screen.Settings.route) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text(stringResource(R.string.settings_nav)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onTertiary,
                            unselectedTextColor = MaterialTheme.colorScheme.onTertiary,
                            indicatorColor = Color.Transparent
                        )
                    )

                }
            }
        },
    ) {paddingValues ->
        NavHost(navController = navController, startDestination = Screen.History.route,
            modifier = Modifier.padding(paddingValues)) {
            composable(Screen.History.route) { ClipboardApp(viewModel) }
            composable(Screen.Favorites.route) { FavoritesScreen(viewModel) }
            composable(Screen.Settings.route) { SettingsScreen(settingsViewModel) }
        }
    }
}
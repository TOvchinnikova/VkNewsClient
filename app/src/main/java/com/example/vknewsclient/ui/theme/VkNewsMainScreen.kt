package com.example.vknewsclient.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vknewsclient.MainViewModel
import com.example.vknewsclient.navigation.AppNavGraph
import com.example.vknewsclient.navigation.Screen

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favourite,
                    NavigationItem.Profile
                )
                items.forEach { item ->
                    BottomNavigationItem(
                        selected = currentRoute == item.screen.route,
                        onClick = {
                            navHostController.navigate(item.screen.route) {
                                popUpTo(Screen.NewsFeed.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navHostController,
            homeScreenContent = {
                HomeScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues
                )
            },
            favouriteScreenContent = { TextCounter("Favourite") },
            profileScreenContent = { TextCounter("Profile") }
        )
    }
}

@Composable
private fun TextCounter(name: String) {
    var count by rememberSaveable {
        mutableStateOf(0)
    }

    Text(
        modifier = Modifier.clickable { count ++ },
        text = "$name Count: $count",
        color = Color.Black
    )
}
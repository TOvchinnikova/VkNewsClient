package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.navigation.Screen.Companion.KEY_FEED_POST_ID

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route
    ) {
        composable(
            route = Screen.NewsFeed.route,
            arguments = listOf(
                navArgument(KEY_FEED_POST_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            newsFeedScreenContent()
        }
        composable(Screen.Comments.route) {
            val feedPostId = it.arguments?.getInt(KEY_FEED_POST_ID) ?: 0
            commentsScreenContent(FeedPost(id = feedPostId))
        }
    }
}
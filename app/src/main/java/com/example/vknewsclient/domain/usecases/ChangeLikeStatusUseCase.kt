package com.example.vknewsclient.domain.usecases

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject


class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    suspend operator fun invoke(feedPost: FeedPost) {
        return repository.changeLikeStatus(feedPost)
    }
}
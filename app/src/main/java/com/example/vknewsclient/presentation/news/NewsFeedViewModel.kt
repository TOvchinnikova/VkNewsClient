package com.example.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.example.vknewsclient.domain.usecases.DeletePostUseCase
import com.example.vknewsclient.domain.usecases.GetRecommendationsUseCase
import com.example.vknewsclient.domain.usecases.LoadNextDataUseCase
import com.example.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    getRecommendationsUseCase: GetRecommendationsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by exception handler")
    }

    private val recommendationsFlow = getRecommendationsUseCase()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    val screenState = recommendationsFlow
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
        .onStart {
            emit(NewsFeedScreenState.Loading)
        }
        .mergeWith(loadNextDataFlow)

    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = recommendationsFlow.value,
                    nextDataIsLoading = true
                )
            )
            loadNextDataUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun remove(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            deletePostUseCase(feedPost)
        }
    }
}
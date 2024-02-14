package com.example.vknewsclient.presentation

import android.app.Application
import com.example.vknewsclient.di.ApplicationComponent
import com.example.vknewsclient.di.DaggerApplicationComponent
import com.example.vknewsclient.domain.entity.FeedPost

class NewsFeedApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this, FeedPost(0, 0, "", "", "", "", "", listOf(), false))
    }
}
package com.android.hackathon.movieapp

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

object AppExecutors {

    private val networkIO = Executors.newScheduledThreadPool(3)

    fun getNetworkIO(): ScheduledExecutorService {
        return networkIO
    }

}
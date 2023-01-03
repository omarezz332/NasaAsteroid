package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.CoroutineWorker
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.PicOfTodayRepository
import retrofit2.HttpException
import timber.log.Timber


class RefreshDataWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val database = getDatabase(context = applicationContext)
        val pictureOfDayRepository = PicOfTodayRepository(database)
        val asteroidsRepository = AsteroidRepository(database)
        Timber.i(asteroidsRepository.asteroidSaved.value.toString())

        return try {
            pictureOfDayRepository.refreshPicOfToday()
            asteroidsRepository.refreshAsteroid()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }


    }


}
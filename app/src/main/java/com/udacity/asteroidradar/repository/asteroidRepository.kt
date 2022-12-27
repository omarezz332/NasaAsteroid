package com.udacity.asteroidradar.repository;

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Utils
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class AsteroidRepository(private val database: NasaDatabase) {
    val today = Utils.convertDateStringToFormattedString(
        Calendar.getInstance().time,
        Constants.API_QUERY_DATE_FORMAT
    )
    val week =
        Utils.convertDateStringToFormattedString(
            Utils.addDaysToDate(Calendar.getInstance().time, 7),
            Constants.API_QUERY_DATE_FORMAT
        )

val asteroidSaved :LiveData<List<Asteroid>> =
    Transformations.map(database.asteroidDao.getAllAsteroid()) {
        it.asDomainModel()
    }
    val asteroidToday :LiveData<List<Asteroid>> =
    Transformations.map(database.asteroidDao.getTodayAsteroid(today)) {
        it.asDomainModel()
    }
    val asteroidWeekly :LiveData<List<Asteroid>> =
    Transformations.map(database.asteroidDao.getWeeklyAsteroid(today,week)) {
        it.asDomainModel()
    }



    suspend fun refreshAsteroid(){
        withContext(Dispatchers.IO){
            try {
                val asteroids = NasaApi.retrofitService.getAsteroids(Constants.api_key).await()
                database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
            } catch (_: Exception) {
            }
        }
    }


}

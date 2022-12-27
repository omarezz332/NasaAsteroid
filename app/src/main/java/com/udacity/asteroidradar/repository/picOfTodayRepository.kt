package com.udacity.asteroidradar.repository;

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class PicOfTodayRepository(private val database: NasaDatabase) {


val picOfToday :LiveData<PictureOfDay> =
    Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
        it.asDomainModel()
    }




    suspend fun refreshPicOfToday(){
        withContext(Dispatchers.IO){
            try {
                val picOfToday = NasaApi.retrofitService.getPicOfDay(Constants.api_key).await()
                database.pictureOfDayDao.insertPictureOfDay(picOfToday.asDatabaseModel())
            } catch (_: Exception) {
            }
        }
    }


}

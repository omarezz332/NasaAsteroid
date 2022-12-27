package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.domain.Asteroid


@Dao
interface PictureOfDayDao {
    @Query(NasaDatabase.GET_PICTURE_OF_DAY)
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(pictureOfDay: DatabasePictureOfDay)

}

@Dao
interface AsteroidDao {

    @Query(NasaDatabase.GET_ALL_NEAR_EARTH_OBJECTS)
    fun getAllAsteroid(): LiveData<List<DatabaseAsteroid>>

    @Query(NasaDatabase.GET_TODAY_OBJECTS)
    fun getTodayAsteroid(today: String): LiveData<List<DatabaseAsteroid>>

    @Query(NasaDatabase.GET_WEEKLY_NEAR_EARTH_OBJECTS)
    fun getWeeklyAsteroid(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)
}

@Database(
    entities = [DatabasePictureOfDay::class, DatabaseAsteroid::class],
    version = 1,
    exportSchema = false
)
abstract class NasaDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "NasaDatabase"
        const val GET_PICTURE_OF_DAY =
            "SELECT * FROM DatabasePictureOfDay pod ORDER BY pod.date DESC LIMIT 0,1"
        const val GET_ALL_NEAR_EARTH_OBJECTS =
            "select * from DatabaseAsteroid order by closeApproachDate desc"
        const val GET_WEEKLY_NEAR_EARTH_OBJECTS =
            "SELECT * FROM DatabaseAsteroid obj WHERE obj.closeApproachDate BETWEEN :startDate  AND :endDate  order by closeApproachDate desc"
        const val GET_TODAY_OBJECTS =
            "SELECT * FROM DatabaseAsteroid obj WHERE obj.closeApproachDate = :today"
    }

    abstract val pictureOfDayDao: PictureOfDayDao
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: NasaDatabase

fun getDatabase(context: Context): NasaDatabase {
    synchronized(NasaDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NasaDatabase::class.java, NasaDatabase.DATABASE_NAME
            ).build()
        }
    }
    return INSTANCE
}







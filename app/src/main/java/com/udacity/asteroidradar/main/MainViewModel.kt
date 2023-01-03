package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.constants.AsteroidApiFilter
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.PicOfTodayRepository
import kotlinx.coroutines.launch
import android.util.Log

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val pictureOfDayRepository = PicOfTodayRepository(database)
    private val asteroidsRepository = AsteroidRepository(database)
    private val filter = MutableLiveData(AsteroidApiFilter.SHOW_SAVED)

    init {
        viewModelScope.launch {
            pictureOfDayRepository.refreshPicOfToday()
            asteroidsRepository.refreshAsteroid()
        }
    }

    val picOfDay = pictureOfDayRepository.picOfToday
    val asteroids = Transformations.switchMap(filter) {
        when (it) {
            AsteroidApiFilter.SHOW_TODAY -> asteroidsRepository.asteroidToday
            AsteroidApiFilter.SHOW_WEEK -> asteroidsRepository.asteroidWeekly
            else -> asteroidsRepository.asteroidSaved
        }
    }

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedProperty: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid


    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }
    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
    fun updateFilter(filter: AsteroidApiFilter){
        this.filter.value=filter
    }

}
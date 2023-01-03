package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.constants.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()


interface NasaApiServices{
    @GET(value="planetary/apod")
     fun getPicOfDay(@Query("api_key")apiKey:String): Deferred<PictureOfDay>
    @GET(value="neo/rest/v1/feed")
     fun getAsteroids(@Query("api_key")apiKey:String): Deferred<String>
}
object NasaApi {
    val retrofitService : NasaApiServices by lazy { retrofit.create(NasaApiServices::class.java) }
}



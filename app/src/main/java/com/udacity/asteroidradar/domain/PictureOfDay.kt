package com.udacity.asteroidradar.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabasePictureOfDay

@JsonClass(generateAdapter = true)
data class PictureOfDay(
    val url: String, val title: String,

    @Json(name = "media_type") val mediaType: String, val date: String,

    )

fun PictureOfDay.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        url, title,
        mediaType, date,
    )
}
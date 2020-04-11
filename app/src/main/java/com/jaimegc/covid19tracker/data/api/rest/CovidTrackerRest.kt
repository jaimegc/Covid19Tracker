package com.jaimegc.covid19tracker.data.api.rest

import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidTrackerRest {

    @GET("{date}")
    suspend fun getCovidTrackerByDate(@Path("date") date: String): CovidTrackerDto
}
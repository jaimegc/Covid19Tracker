package com.jaimegc.covid19tracker.data.api.rest

import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidTrackerRest {

    @GET("{date}")
    suspend fun getCovidTrackerByDate(@Path("date") date: String): CovidTrackerDto

    @GET("{date}")
    suspend fun getCovidTrackerByDateAsResponse(@Path("date") date: String): Response<CovidTrackerDto>
}
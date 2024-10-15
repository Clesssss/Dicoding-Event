package com.example.myproject.data.retrofit

import com.example.myproject.data.response.EventResponse
import com.example.myproject.data.response.EventResponse2
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events/{id}")
    fun getEvent(
        @Path("id") id: String
    ): Call<EventResponse2>

    @GET("events?active=1")
    fun getUpcomingEvents(): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvents(): Call<EventResponse>

    @GET("events")
    fun getFinishedEvents(
        @Query("q") query: String,
        @Query("active") active: Int = 0 // Other parameters as needed
    ): Call<EventResponse>

    @GET("events")
    fun getUpcomingEvents(
        @Query("q") query: String,
        @Query("active") active: Int = 1 // Other parameters as needed
    ): Call<EventResponse>

    @GET("events?active=1&limit=5")
    fun getUpcomingEventsLimit5(): Call<EventResponse>

    @GET("events?active=0&limit=5")
    fun getFinishedEventsLimit5(): Call<EventResponse>

}
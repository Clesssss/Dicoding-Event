package com.example.myproject.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproject.data.response.EventResponse
import com.example.myproject.data.response.ListEventsItem
import com.example.myproject.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem?>?>()
    val upcomingEvents: LiveData<List<ListEventsItem?>?> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem?>?>()
    val finishedEvents: LiveData<List<ListEventsItem?>?> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    fun fetchUpcomingEvents() {
        if (_upcomingEvents.value.isNullOrEmpty()) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getUpcomingEventsLimit5()
            client.enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val events = response.body()?.listEvents
                        if (events.isNullOrEmpty()) {
                            _errorMessage.value = "No upcoming events available"
                        } else {
                            _upcomingEvents.value = events
                        }
                    } else {
                        Log.e("HomeViewModel", "onFailure: ${response.message()}")
                        _errorMessage.value = "Failed to load upcoming events: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("HomeViewModel", "onFailure: ${t.message.toString()}")
                    _errorMessage.value = "No internet connection"
                }
            })
        }
    }

    fun fetchFinishedEvents() {
        if (_finishedEvents.value.isNullOrEmpty()) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getFinishedEventsLimit5()
            client.enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val events = response.body()?.listEvents
                        if (events.isNullOrEmpty()) {
                            _errorMessage.value = "No finished events available"
                        } else {
                            _finishedEvents.value = events
                        }
                    } else {
                        Log.e("HomeViewModel", "onFailure: ${response.message()}")
                        _errorMessage.value = "Failed to load finished events: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("HomeViewModel", "onFailure: ${t.message.toString()}")
                    _errorMessage.value = "No internet connection"
                }
            })
        }
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
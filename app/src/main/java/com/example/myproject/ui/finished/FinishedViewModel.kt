package com.example.myproject.ui.finished

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

class FinishedViewModel : ViewModel() {

    private val _finishedEvents = MutableLiveData<List<ListEventsItem?>?>()
    val finishedEvents: LiveData<List<ListEventsItem?>?> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchFinishedEvents() {
        if (_finishedEvents.value.isNullOrEmpty()) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getFinishedEvents() // Assuming the ApiService provides this method.
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
                        Log.e("FinishedViewModel", "onFailure: ${response.message()}")
                        _errorMessage.value = "Failed to load finished events: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("FinishedViewModel", "onFailure: ${t.message.toString()}")
                    _errorMessage.value = "No internet connection"
                }
            })
        }
    }
    fun searchEvents(query: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFinishedEvents(query) // Pass the search query here
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    // Update the finishedEvents LiveData with the new filtered list
                    _finishedEvents.value = response.body()?.listEvents
                } else {
                    Log.e("FinishedViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("FinishedViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
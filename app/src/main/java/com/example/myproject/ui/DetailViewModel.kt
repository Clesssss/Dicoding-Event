package com.example.myproject.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.response.Event
import com.example.myproject.data.response.EventResponse2
import com.example.myproject.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(eventId: String) : ViewModel(){

    private val _eventDetail = MutableLiveData<Event?>()
    val eventDetail: LiveData<Event?> get() = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        fetchEvent(eventId)
    }
    private fun fetchEvent(eventId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(eventId)
        client.enqueue(object : Callback<EventResponse2> {
            override fun onResponse(
                call: Call<EventResponse2>,
                response: Response<EventResponse2>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventDetail.value = response.body()?.event
                } else {
                    Log.e("MainViewModel", "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load event details. Try again later."
                }
            }

            override fun onFailure(call: Call<EventResponse2>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Unable to connect. Please check your network connection."
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
class DetailViewModelFactory(private val eventId: String) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

package io.ionic.concurrencytestapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpdateViewModel : ViewModel() {
    private val _enabled = MutableLiveData(true)
    private val _startTime = MutableLiveData<String>()
    private val _stopTime = MutableLiveData<String>()
    private val _duration = MutableLiveData<String>()
    private val _status = MutableLiveData<String>()

    val enabled: LiveData<Boolean> = _enabled
    val startTime: LiveData<String> = _startTime
    val stopTime: LiveData<String> = _stopTime
    val duration: LiveData<String> = _duration
    val status: LiveData<String> = _status

    fun syncAllAtOnce() {
        Log.d("UpdateViewModel", "syncAllAtOnce")
        _enabled.value = false
        _status.value = "Syncing all at once"
    }

    fun syncStaggered() {
        Log.d("UpdateViewModel", "syncStaggered")
        _enabled.value = false
        _status.value = "Syncing staggered"
    }
}
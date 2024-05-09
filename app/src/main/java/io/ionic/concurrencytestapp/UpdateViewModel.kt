package io.ionic.concurrencytestapp

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ionic.liveupdates.LiveUpdate
import io.ionic.liveupdates.LiveUpdateManager
import io.ionic.liveupdates.Strategy
import io.ionic.liveupdates.data.model.FailResult
import io.ionic.liveupdates.data.model.SyncResult
import io.ionic.liveupdates.network.SyncCallback
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class UpdateViewModel : ViewModel() {
    private val timeFormat = "hh:mm:ss.SSSS"

    private val _enabled = MutableLiveData(true)
    private val _log  = mutableStateListOf<String>()

    val enabled: LiveData<Boolean> = _enabled
    val log = _log

    private val liveUpdates: MutableList<LiveUpdate> = mutableListOf()

    init {
        liveUpdates.add(LiveUpdate("34df4710", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("17633623", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("94d0dfae", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("a7b10ac1", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("186b544f", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("8f0d1d90", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("8c5be05b", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("54c549cd", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("b8ae902b", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("74ef3e96", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("ebd6138b", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("a41075c0", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("3fde24f8", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("f76eb498", "production", false, Strategy.ZIP))
        liveUpdates.add(LiveUpdate("48d8d9e9", "production", false, Strategy.ZIP))
    }

    fun syncAllAtOnce(context: Context) {
        Log.d("UpdateViewModel", "syncAllAtOnce")
        reset(context)

        _enabled.value = false

        val startTime = DateTimeFormatter
            .ofPattern(timeFormat)
            .withZone(ZoneOffset.UTC)
            .format(Instant.now())

        Log.d("UpdateViewModel", "Start time: $startTime")
        _log.add("Start time: $startTime")

        viewModelScope.launch {
            liveUpdates.forEach { liveUpdate -> LiveUpdateManager.addLiveUpdateInstance(context, liveUpdate) }
            LiveUpdateManager.sync(context, callback = object : SyncCallback {
                override fun onAppComplete(syncResult: SyncResult) {
                    Log.d("LUAppComplete", "App ${syncResult.liveUpdate.appId} completed successfully!")
                    _log.add("App ${syncResult.liveUpdate.appId} completed successfully!")
                }

                override fun onAppComplete(failResult: FailResult) {
                    Log.d("LUAppComplete", "App ${failResult.liveUpdate.appId} failed with error ${failResult.failMsg}.")
                    _log.add("App ${failResult.liveUpdate.appId} failed with error ${failResult.failMsg}.")
                }

                override fun onSyncComplete() {
                    Log.d("LUAppComplete", "Sync complete!")
                    _log.add("Sync complete!")
                }
            })
        }
    }

    fun syncStaggered(context: Context) {
        Log.d("UpdateViewModel", "syncStaggered")
        reset(context)

        _enabled.value = false

        val startTime = DateTimeFormatter
            .ofPattern(timeFormat)
            .withZone(ZoneOffset.UTC)
            .format(Instant.now())

        Log.d("UpdateViewModel", "Start time: $startTime")
        _log.add("Start time: $startTime")

        viewModelScope.launch {
            liveUpdates.forEach { liveUpdate -> LiveUpdateManager.addLiveUpdateInstance(context, liveUpdate) }

            viewModelScope.launch {
                liveUpdates.forEach {
                    val individualStartTime = DateTimeFormatter
                        .ofPattern(timeFormat)
                        .withZone(ZoneOffset.UTC)
                        .format(Instant.now())

                    Log.d("UpdateViewModel", "Starting update for ${it.appId} at $individualStartTime")
                    _log.add("Starting update for ${it.appId} at $individualStartTime")

                    LiveUpdateManager.sync(context, it.appId, object : SyncCallback {
                        override fun onAppComplete(syncResult: SyncResult) {
                            val completeTime = DateTimeFormatter
                                .ofPattern(timeFormat)
                                .withZone(ZoneOffset.UTC)
                                .format(Instant.now())

                            Log.d("LUAppComplete", "App ${syncResult.liveUpdate.appId} completed successfully at $completeTime.")
                            _log.add("App ${syncResult.liveUpdate.appId} completed successfully at $completeTime.")
                        }

                        override fun onAppComplete(failResult: FailResult) {
                            Log.d("LUAppComplete", "App ${failResult.liveUpdate.appId} failed with error ${failResult.failMsg}.")
                            _log.add("App ${failResult.liveUpdate.appId} failed with error ${failResult.failMsg}.")
                        }

                        override fun onSyncComplete() {

                        }
                    })
                }
            }
        }
    }

    fun reset(context: Context) {
        LiveUpdateManager.reset(context)

        _enabled.value = true
        _log.clear()
    }
}
package com.example.newsapi.repository

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapi.R
import com.example.newsapi.api.ApiInterface
import com.example.newsapi.model.TopHeadLineResponse
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.util.*


class HeadlinesRepository(
    private val apiInterface: ApiInterface,
    private val applicationContext: Context
) {
    private val headlineLiveData = MutableLiveData<TopHeadLineResponse>()
    val headlines: LiveData<TopHeadLineResponse>
        get() = headlineLiveData

    suspend fun getHeadlineRespose(country: String) {
        val result = apiInterface.getHeadlines(country)
        if (result.body() != null) {
            headlineLiveData.postValue(result.body())
        }

    }



    fun showAdds(): Boolean {
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(20)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            val updated = task.result
            if (task.isSuccessful) {
                val updated = task.result
                Log.d("TAG", "Config params updated: $updated")
            } else {
                Log.d("TAG", "Config params updated: $updated")
            }
        }

        return mFirebaseRemoteConfig.getBoolean("showAdds")
    }




    fun getUserCountry(): String? {
        try {
            val tm =
                applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US)
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US)
                }
            }
        } catch (e: Exception) {
        }
        return null
    }

}
package com.ditadigitali.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("settings")

object NetworkUtil {
    private val SERVER_IP = stringPreferencesKey("server_ip")

    fun getServerIp(context: Context): String = runBlocking {
        val prefs = context.dataStore.data.first()
        prefs[SERVER_IP] ?: "10.42.0.2"
    }

    fun saveServerIp(context: Context, ip: String) = runBlocking {
        context.dataStore.edit { it[SERVER_IP] = ip }
    }

    fun getRequestUrl(context: Context): String {
        val ip = getServerIp(context)
        return "http://$ip:8080/index.html"
    }

    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

package com.example.myfriendbinny.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

// Create DataStore instance
val Context.dataStore by preferencesDataStore(name = "bin_data_store")

object BinDataStore {

    private val BIN_LIST_KEY = stringPreferencesKey("bin_list_key")

    // Save list of bins
    suspend fun saveBins(context: Context, bins: List<BinData>) {
        val jsonBins = Json.encodeToString(bins)
        context.dataStore.edit { preferences ->
            preferences[BIN_LIST_KEY] = jsonBins
        }
    }

    // Read list of bins
    fun readBins(context: Context): Flow<List<BinData>> {
        return context.dataStore.data.map { preferences ->
            val jsonBins = preferences[BIN_LIST_KEY] ?: "[]"
            Json.decodeFromString(jsonBins)
        }
    }
}

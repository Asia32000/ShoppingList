import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("listName")
        private val LIST_NAME_KEY = stringPreferencesKey("listName")
        private val COLOR_NAME_KEY = stringPreferencesKey("colorName")
    }

    val getListName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LIST_NAME_KEY] ?: "Shopping List"
    }

    suspend fun saveListName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[LIST_NAME_KEY] = name
        }
    }

    val getColorName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[COLOR_NAME_KEY] ?: "Blue"
    }

    suspend fun saveColorName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[COLOR_NAME_KEY] = name
        }
    }
}
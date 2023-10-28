package com.student.prefrencedatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var datStore: DataStore<Preferences>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        datStore = createDataStore(name = "my_data_store")

        val name:EditText = findViewById(R.id.name)
        val age:EditText = findViewById(R.id.age)
        val btnSubmit: Button = findViewById(R.id.button_submit)
        val btnLoad:Button = findViewById(R.id.button_load)
        val readName:EditText = findViewById(R.id.read_name)
        val readAge:TextView = findViewById(R.id.read_age)


        btnSubmit.setOnClickListener {
                lifecycleScope.launch {
                    saveData(name.text.toString(),age.text.toString().toInt())
                }
        }

        btnLoad.setOnClickListener {
                lifecycleScope.launch {
                val data = loadData(readName.text.toString())
                readAge.text = data ?: "No Data Found"
            }
        }
    }

    suspend fun saveData(name:String,age:Int){
            val dataStoreKey = preferencesKey<String>(name)
            datStore.edit {
                it[dataStoreKey] = age.toString()
            }
    }

    suspend fun loadData(name: String):String?{
        val dataStoreKey = preferencesKey<String>(name)
        val preferences = datStore.data.first()
        return preferences[dataStoreKey]
    }
}
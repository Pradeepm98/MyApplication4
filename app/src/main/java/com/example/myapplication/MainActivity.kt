package com.example.myapplication

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val a = "Hello, World!"
        // Example of a call to a native method
        val result = utils.stringFromJNI() + " " + utils.helloFromKotlin().toString()
        binding.SAMPLETEXT.text = "Loading..."

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val check = utils.websiteRequest()
                Log.d("MainActivity", "check value: $check")

                // Update the UI on the main thread
                launch(Dispatchers.Main) {
                    binding.SAMPLETEXT.text = check
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "An error occurred: ${e.message}")
            }
        }
    }

    companion object {
        // Used to load the 'myapplication' library on application startup.
        init {
            System.loadLibrary("myapplication")
        }
    }
}

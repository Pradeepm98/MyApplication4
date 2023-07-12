package com.example.myapplication

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.myapplication.dynamicserver
import kotlinx.coroutines.DelicateCoroutinesApi

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val a = "Hello, World!"
        // Example of a call to a native method
        val result = utils.stringFromJNI() + " " + utils.helloFromKotlin().toString()
        binding.loading.text = "Loading..."

        GlobalScope.launch(Dispatchers.IO) {
            try {

                    val check = dynamicserver.startProxyServer()//newtemp()//serverworker()
                    //utils.websiteRequest()

                    Log.d("MainActivity", "check value: $check")

                    // Update the UI on the main thread
                    launch(Dispatchers.Main) {
                        binding.loading.text = check.toString()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "An error occurred: ${e.message}")
            }
        }

        // Load the saved text, if any
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedText = sharedPreferences.getString("saved_text", "")
        binding.userInputEditText.setText(savedText)

        // Set an onClickListener for the save button
        binding.saveButton.setOnClickListener {
            // Save the entered text
            val userInput = binding.userInputEditText.text.toString()
            val editor = sharedPreferences.edit()
            editor.putString("saved_text", userInput)
            editor.apply()

            // Show a toast indicating the text has been saved
            Toast.makeText(this@MainActivity, "Text saved!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        // Used to load the 'myapplication' library on application startup.
        init {
            System.loadLibrary("myapplication")
        }
    }
}

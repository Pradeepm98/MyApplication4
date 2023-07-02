package com.example.myapplication
import android.content.ContentValues.TAG
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL
import java.security.MessageDigest



class utils {
    private val TAG = "utils"
    companion object {
        fun stringFromJNI(): String {
            return "Hello from Kotlin"
        }

        fun helloFromKotlin(): Byte {
            return 0x01
        }
        fun calculateSHA256(input: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val hashBytes = md.digest(input.toByteArray())
            return bytesToHex(hashBytes)
        }

        fun bytesToHex(bytes: ByteArray): String {
            val hexChars = "0123456789ABCDEF"
            val result = StringBuilder(bytes.size * 2)
            for (byte in bytes) {
                val i = byte.toInt() and 0xFF
                result.append(hexChars[i ushr 4])
                result.append(hexChars[i and 0x0F])
            }
            return result.toString()
        }
        fun websiteRequest(): String {
            val url = URL("https://jsonip.com/")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            println("Response Code: $responseCode")

            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            var inputLine: String?
            val response = StringBuilder()

            while (bufferedReader.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }

            bufferedReader.close()



            return response.toString()
        }

    }
}


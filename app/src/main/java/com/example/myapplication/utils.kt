package com.example.myapplication
import android.content.ContentValues.TAG
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL
import java.security.MessageDigest
import javax.net.ssl.SSLSocketFactory
import com.example.myapplication.dynamicserver


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
        val userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1 Mobile/15E148 Safari/604.1"
        fun websiteRequest(): String {
            val host = "www.whatsmyua.info"
            val port = 443

            val sslSocketFactory = SSLSocketFactory.getDefault()
            val socket = sslSocketFactory.createSocket(host, port) as Socket

            val request = "GET / HTTP/1.1\r\n" +
                    "Host: $host\r\n" +
                    "User-Agent: $userAgent\r\n" +
                    "Connection: close\r\n\r\n"

            val writer = PrintWriter(socket.getOutputStream())
            writer.print(request)
            writer.flush()

            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val response = StringBuilder()

            var inputLine: String?
            while (reader.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }

            writer.close()
            reader.close()
            socket.close()

            return response.toString().substring(500)
        }



//        fun websiteRequest(): String {
//            val host = "jsonip.com"
//            val port = 80
//
//            val socket = Socket(host, port)
//
//            val request = "GET / HTTP/1.1\r\n" +
//                    "Host: $host\r\n" +
//                    "Connection: close\r\n\r\n"
//
//            val writer = PrintWriter(socket.getOutputStream())
//            writer.print(request)
//            writer.flush()
//
//            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
//            val response = StringBuilder()
//
//            var inputLine: String?
//            while (reader.readLine().also { inputLine = it } != null) {
//                response.append(inputLine)
//            }
//
//            writer.close()
//            reader.close()
//            socket.close()
//
//            return response.toString()
//        }
//        fun websiteRequest(): String {
//            val url = URL("https://jsonip.com/")
//            val connection = url.openConnection() as HttpURLConnection
//            connection.requestMethod = "GET"
//
//            val responseCode = connection.responseCode
//            println("Response Code: $responseCode")
//
//            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
//            var inputLine: String?
//            val response = StringBuilder()
//
//            while (bufferedReader.readLine().also { inputLine = it } != null) {
//                response.append(inputLine)
//            }
//
//            bufferedReader.close()
//
//
//
//            return response.toString()
//        }

    }
}


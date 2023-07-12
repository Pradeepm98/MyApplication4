package com.example.myapplication
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.io.PrintWriter
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.fixedRateTimer

import java.net.ServerSocket
import kotlin.concurrent.thread
import java.io.InputStream






class dynamicserver {
    companion object {
        fun serverworker(username:String) {
            val clientSocket = Socket("13.127.87.242", 3000)

            while (true) {
                handleRequest(clientSocket,username)
            }
        }

        fun handleRequest(clientSocket: Socket,username: String) {

            println("Connected to server")


            val data = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            val requestData = data.readLine().toByteArray(Charsets.UTF_8)
            println("Request: ${String(requestData, Charsets.UTF_8)}")
            val dataStr = String(requestData, Charsets.UTF_8)

            val firstLine = dataStr.split('\n')[0]
            val url = firstLine.split(' ')[1]

            println("URL: $url")

            val httpPos = url.indexOf("://")
            val temp = if (httpPos == -1) url else url.substring(httpPos + 3)

            val portPos = temp.indexOf(":")
            var webServerPos = temp.indexOf("/")
            if (webServerPos == -1) {
                webServerPos = temp.length
            }

            val webServer = if (portPos == -1 || webServerPos < portPos) {
                temp.substring(0, webServerPos)
            } else {
                temp.substring(0, portPos)
            }

            val port = if (portPos == -1 || webServerPos < portPos) {
                80
            } else {
                temp.substring(portPos + 1, webServerPos).toInt()
            }

            println("Web Server: $webServer")
            println("Port: $port")
            // val hoste = "jsonip.com"
            // val porte = 80

            // Establish a socket connection to the server

            val serverSocket = Socket(webServer, port)
            // val serverSocket = Socket(webServer, port)
            println("Connected to web server")
            println(serverSocket)


            val request = "GET / HTTP/1.1\r\n" +
                    "Host: $webServer\r\n" +
                    "Connection: close\r\n" +
                    "\r\n"

            val writer = BufferedWriter(OutputStreamWriter(serverSocket.getOutputStream()))
            //print type of request
            println("Request: $request")
            writer.write(request)
            writer.flush()

            println("Request sent to web server")

            // serverOutputStream.write(requestData)
            // serverOutputStream.flush()

            val reply = serverSocket.getInputStream().readBytes()
            clientSocket.getOutputStream().write(reply)
            clientSocket.getOutputStream().flush()



            val reader = BufferedReader(InputStreamReader(serverSocket.getInputStream()))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }

            serverSocket.close()
        }


        fun temp() {
            val serverIp = "65.0.107.42" // Replace with the server's IP address
            val serverPort = 12345

            // Set up the client
            val clientSocket = Socket()
            clientSocket.connect(InetSocketAddress(serverIp, serverPort))

            val writer = PrintWriter(clientSocket.getOutputStream(), true)
            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))

            // Send messages to the server
            while (true) {
                sleep(10000) // 10 seconds

                val message = "inputdferfrfrefrfvrfrvfrvvfgggggggggggg"
                writer.println(message)
                if (message == "quit") {
                    break
                }

                // Receive a response from the server
                val response = reader.readLine()
                println("Server: $response")
            }

            // Close the connection
            clientSocket.close()
        }

        fun newtemp() {
            val clientSocket = Socket("65.0.107.42", 12345) // Replace with the server's IP address and port
            val random = Random()

            val timer = fixedRateTimer(period = 10000) {
                val message = generateRandomString(1000)
                clientSocket.getOutputStream().write(message.toByteArray())
                if (message == "quit") {
                    cancel()
                }
            }

            val responseBuffer = ByteArray(1024)
            val inputStream = clientSocket.getInputStream()
            while (true) {
                val bytesRead = inputStream.read(responseBuffer)
                if (bytesRead == -1) {
                    break
                }
                val response = String(responseBuffer, 0, bytesRead)
                println("Server: $response")
            }

            inputStream.close()
            clientSocket.close()
        }

        fun generateRandomString(length: Int): String {
            val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            return (1..length)
                .map { characters.random() }
                .joinToString("")
        }


        fun handleClient(clientSocket: Socket) {
            val request = clientSocket.getInputStream().bufferedReader().readLine()
            val firstLine = request.substringBefore("\r\n")
            val method = firstLine.split(" ")[0]
            println("Request method: $method")
            println("Request line: $firstLine")

            if (method == "CONNECT") {
                // Extract the requested host and port
                val hostPort = firstLine.split(" ")[1]
                val (host, port) = hostPort.split(":")

                // Create a connection to the requested server
                val serverSocket = Socket(host, port.toInt())

                // Send the client a success response//
                //clientSocket.getOutputStream().write("HTTP/1.1 200 OK\r\n\r\n".toByteArray())

                // Start forwarding data between client and server
                thread { forwardData(clientSocket.getInputStream(), serverSocket.getOutputStream()) }
                thread { forwardData(serverSocket.getInputStream(), clientSocket.getOutputStream()) }
            } else {

                println("Request line: $request")
                // Handle regular HTTP requests


                // val host = firstLine.split(" ")[1].removePrefix("http://").removeSuffix("/")
                // println("Request host: $host")

                // // Create a connection to the target server on port 80
                // val serverSocket = Socket(host, 80)

                // println("Connected to $host")

                // // Forward the client's request to the server
                // serverSocket.getOutputStream().write(request.toByteArray())

                // // Wait for a second before forwarding the data
                // Thread.sleep(1000)

                // // Forward the server's response back to the client
                // thread { forwardData(serverSocket.getInputStream(), clientSocket.getOutputStream()) }
            }
        }

        fun forwardData(sourceStream: InputStream, destinationStream: OutputStream) {
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (sourceStream.read(buffer).also { bytesRead = it } != -1) {
                destinationStream.write(buffer, 0, bytesRead)
            }
        }

        fun startProxyServer() {
//val proxyServer = ServerSocket(8001)
//  val clientSocket = Socket("127.0.0.1", 3000)
//         val clientAddress = clientSocket.remoteSocketAddress
//         println("Received connection from $clientAddress")

            while (true) {
                println("Proxy server listening on port 8001")
                val clientSocket = Socket("13.127.87.242", 3006)
                val clientAddress = clientSocket.remoteSocketAddress
                println("Received connection from $clientAddress")
                // thread { handleClient(clientSocket) }
                handleClient(clientSocket)
                println("thread started\n")

            }
        }






    }
}
package com.example.myapplication

import java.net.Socket




class dynamicserver {
    companion object {
        fun serverworker() {
            val clientSocket = Socket("13.127.87.242", 3000)

            while (true) {
                newFunc(clientSocket)
            }
        }

        fun newFunc(clientSocket: Socket) {
            val data = clientSocket.getInputStream().readBytes()
            val dataStr = data.decodeToString()
            val firstLine = dataStr.split('\n')[0]
            val url = firstLine.split(' ')[1]
            val httpPos = url.indexOf("://")
            val temp = if (httpPos == -1) url else url.substring(httpPos + 3)
            val portPos = temp.indexOf(":")
            var webserverPos = temp.indexOf("/")
            if (webserverPos == -1) {
                webserverPos = temp.length
            }
            val webserver = temp.substring(0, webserverPos)
            val port = if (portPos == -1 || webserverPos < portPos) {
                80
            } else {
                temp.substring(portPos + 1, webserverPos).toInt()
            }
            val s = Socket(webserver, port)
            s.soTimeout = 10000
            s.getOutputStream().write(data)
            val reply = s.getInputStream().readBytes()
            clientSocket.getOutputStream().write(reply)
            //s.close()
        }

    }
}
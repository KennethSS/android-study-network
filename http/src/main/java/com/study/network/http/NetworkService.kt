package com.study.network.http

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.security.SecureRandom
import java.util.logging.Logger
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Copyright 2020 Kenneth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/
class NetworkService {
    private fun trustAllHosts() {
        val easyTrustManager: X509TrustManager = object : X509TrustManager {


            override fun checkClientTrusted(
                chain: Array<out java.security.cert.X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out java.security.cert.X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()

        }

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(easyTrustManager)

        // Install the all-trusting trust manager
        try {
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getNetwork(urlPath: String) {
        trustAllHosts()
        val url = URL(urlPath)
        val conn = (url.openConnection() as HttpsURLConnection)
        conn.run {
            requestMethod = REQUEST_GET
            setRequestProperty("Cache-Control", "no-cache")
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")


            //setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connectTimeout = 10000
            doInput = true
            doOutput = true
            connect()
        }

        val responseCode = conn.responseCode


        //Log.d("NetworkService", "Response Code ${conn.responseCode}")




        when (responseCode) {
            HttpsURLConnection.HTTP_OK -> {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                var inputLine: String = ""
                val response = StringBuffer()
                while ((reader.readLine()) != null) {
                    response.append(inputLine)
                }

                reader.close()

                println("NetworkService" + response.toString())

            }
            else -> {
                val error = inputStreamToString(conn.errorStream).toString()
                println("NetworkService" + "Failure ${responseCode}")
                println("NetworkService" + "Failure ${conn.errorStream}")
                println("NetworkService" + "Failure ${error}")
            }
        }
    }

    private fun inputStreamToString(inputStream: InputStream): StringBuffer {
        val reader = BufferedReader(InputStreamReader(inputStream))
        var inputLine: String? = ""
        val response = StringBuffer()
        while (inputLine != null) {
            inputLine = reader.readLine()
            if (inputLine != null) {
                response.append(inputLine)
            }
        }

        reader.close()

        return response
    }
}
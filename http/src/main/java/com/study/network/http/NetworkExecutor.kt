package com.study.network.http

import com.study.network.http.converter.JsonConverter
import com.study.network.http.request.Request
import com.study.network.http.request.RequestMethod
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.reflect.ParameterizedType
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class NetworkExecutor(
    private val host: String,
    private val timeout: Int = 1000 * 10
) {
    private val jsonConverter: JsonConverter by lazy { JsonConverter() }

    fun <T : Any> request(
        request: Request,
        cls: Class<T>,
        callback: (result: NetworkState<T>) -> Unit
    ) {

        runCatching {
            val conn = getConnection(getFullUrl(request))
            requestConn(conn, request)

            val buffered = BufferedReader(InputStreamReader(conn.inputStream, UTF_8))

            val response = buffered.use(BufferedReader::readText)

            println("Response Code: ${conn.responseCode}")
            //println("Response ${response}")

            when (conn.responseCode) {
                in 200..299 -> {
                    if (response.isNotEmpty()) {

                        if (response.startsWith("[")) {
                            val jsonArray = JSONArray(response)

                            val component = cls.componentType
                            val instance = java.lang.reflect.Array.newInstance(component, jsonArray.length())

                            if (jsonArray.length() > 0) {
                                for (i in 0 until jsonArray.length()) {
                                    val d = jsonConverter.convertToObject(jsonArray.getJSONObject(i), component)
                                    java.lang.reflect.Array.set(instance, i, d)
                                }
                            }

                            callback(NetworkState.Success(instance as T))
                        } else {
                            val json = JSONObject(response)
                            println("response $response")

                            val instance = cls.newInstance()
                            jsonConverter.convertToObject(json, cls, instance)
                            callback(NetworkState.Success(instance))
                        }
                    } else {
                        val any = NetworkState.Success(cls.newInstance())
                        callback(any)
                    }
                }
                else -> {
                    callback(NetworkState.Error(Throwable(response)))
                }
            }
        }.onFailure {
            it.printStackTrace()
            callback(NetworkState.Error(it))
        }
    }

    private fun getConnection(urlString: String): HttpURLConnection {
        val url = URL(urlString)

        if (urlString.contains(HTTPS_SCHEME)) {
            trustAllHosts()
            (url.openConnection() as HttpsURLConnection).let { httpsUrlConn ->
                httpsUrlConn.setHostnameVerifier { hostname, sslSession ->
                    true
                }
                return httpsUrlConn
            }
        } else {
            return url.openConnection() as HttpURLConnection
        }
    }

    private fun requestConn(
        conn: HttpURLConnection,
        request: Request,
    ) {

        conn.requestMethod = request.method
        conn.connectTimeout = timeout
        conn.allowUserInteraction = true

        for (header in request.headers) {
            conn.setRequestProperty(header.key, header.value)
        }

        conn.setRequestProperty("Content-type", "application/json")

        when (request.method) {
            RequestMethod.GET -> { }
            RequestMethod.POST -> {
                conn.doInput = true
                conn.doOutput = true

                when(request.paramType) {
                    ParameterType.MULTI_PART -> {
                        conn.setRequestProperty(CONTENT_TYPE, "multipart/form-data; boundary=\"$BOUNDARY\"")
                        val output = conn.outputStream
                        val writer = PrintWriter(OutputStreamWriter(output, UTF_8), true)

                        request.params.forEach {
                            println("MultiPart ${it.key} : ${it.value}")


                            addTextPart(writer, it.key, it.value.toString())
                        }

                        output.flush()
                        writer.append("--$BOUNDARY--").append(LINE_FEED)
                        writer.close()
                    }
                    ParameterType.JSON -> {
                        writeOutputStreamToUtf8(
                            conn.outputStream,
                            request.params.paramsToString(request.paramType)
                        )
                    }
                }
            }
        }
    }

    private fun writeOutputStreamToUtf8(outputStream: OutputStream, param: String) {
        BufferedWriter(OutputStreamWriter(outputStream, UTF_8)).run {
            write(param)
            close()
        }
        outputStream.close()
    }

    private fun trustAllHosts() {
        val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }
        })

        try {
            val sc = SSLContext.getInstance(TLS)
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getFullUrl(request: Request): String {
        return if (request.method == RequestMethod.GET) {
            host + request.endPoint + request.params.paramsToString()
        } else {
            host + request.endPoint
        }
    }

    private fun HashMap<String, Any>.paramsToString(paramType: ParameterType = ParameterType.KEY_VALUE): String {

        val builder = StringBuilder()
        if (isNotEmpty()) {
            when(paramType) {
                ParameterType.KEY_VALUE -> {
                    builder.append("?")

                    onEachIndexed { index, entry ->
                        builder.append(entry.key)
                        builder.append("=")
                        builder.append(entry.value)

                        if (index != size - 1) {
                            builder.append("&")
                        }
                    }
                }
                ParameterType.JSON -> {
                    val json = JSONObject().also { jsonObject ->
                        forEach { entry ->
                            jsonObject.put(entry.key, entry.value)
                        }
                    }.toString()
                    builder.append(json)
                }
            }

        }
        return builder.toString()
    }

    private fun addTextPart(writer: PrintWriter, name: String, value: String?) {
        writer.append("--$BOUNDARY").append(LINE_FEED)
        writer.append("Content-Disposition: form-data; name=\"$name\"").append(LINE_FEED)
        writer.append("Content-Type: text/plain; charset=$UTF_8").append(LINE_FEED)
        writer.append(LINE_FEED)
        writer.append(value).append(LINE_FEED)
        writer.flush()
    }


    companion object {
        const val HTTPS_SCHEME = "https://"
        const val UTF_8 = "UTF-8"
        const val TLS = "TLS"
        const val LINE_FEED = "\r\n"
        const val BOUNDARY = "aJ123Af2318"

        const val CONTENT_TYPE = "Content-Type"
    }
}
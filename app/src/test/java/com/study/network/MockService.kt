package com.study.network

import com.google.common.io.Resources
import okhttp3.OkHttpClient
import java.io.File
import java.net.URL

private val okHttpClient: OkHttpClient
  get() = OkHttpClient.Builder().build()

@Suppress("UnstableApiUsage")
internal fun getJson(path: String): String {
  val uri: URL = Resources.getResource(path)
  val file = File(uri.path)
  return String(file.readBytes())
}

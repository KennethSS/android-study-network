package com.study.network

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.study.network.retrofit.RetrofitClient
import com.study.network.service.BasicService
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.security.Permission

class MainActivity : AppCompatActivity() {

  val service: BasicService = RetrofitClient.provideService()
  private val MULTIPART_FORM = "multipart/form-data".toMediaTypeOrNull()
  private val IMAGE_PARAM = "image"

  var uri: Uri? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
      findViewById<Button>(R.id.upload).setOnClickListener {

        val file = File(getPath(uri, contentResolver))
        val requestBody = RequestBody.create(MULTIPART_FORM, file)
        val body = MultipartBody.Part.createFormData(IMAGE_PARAM, file.name, requestBody)

        val image = RequestBody.create(MULTIPART_FORM, file)
        val text = RequestBody.create("text/plain".toMediaTypeOrNull(), "abc")

        lifecycleScope.launch {
          runCatching {
            service.postFeed(
              "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjI4MjMzMjQwLCJqdGkiOiJhYWE2NmI3OTNkMjE0MTE5ODRjZjYxYzViODA3NWNhYiIsInVzZXJfaWQiOjF9.U3PJ0U5Qt8cnSk1FEdxfzN0fJjY9cRo03ApckSTlfqc",
              body,
              hashMapOf("text" to text)
            )
          }.onFailure {
            Log.d("MainActivity", "Failure ")
            Log.d("MainActivity", "${it.message}")
            it.stackTrace.forEach {
              Log.d("MainActivity", "${it}")
            }
          }.onSuccess {
            Log.d("MainActivity", "Success")
          }
        }
      }

    findViewById<Button>(R.id.image).setOnClickListener {
      Matisse.from(this@MainActivity)
        .choose(MimeType.ofAll())
        .countable(true)
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .imageEngine(GlideEngine())
        .maxSelectable(4)
        .showPreview(false) // Default is `true`
        .forResult(0)
    }
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)

    when (requestCode) {
      0 -> onResultImageList(resultCode, data)
    }
  }

  private fun onResultImageList(
    resultCode: Int,
    data: Intent?
  ) {
    if (resultCode == RESULT_OK) {
      val selected = Matisse.obtainResult(data)


      if (selected.isNotEmpty()) {
        uri = selected.first()
      }
    }
  }

  private fun getPath(
    uri: Uri?,
    contentResolver: ContentResolver
  ): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)

    val cursor =
      contentResolver.query(uri ?: Uri.parse(""), projection, null, null, null) ?: return ""
    val column_index =
      cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val s = cursor.getString(column_index)
    cursor.close()
    return s
  }
}
package com.study.network.retrofit

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.study.network.retrofit.service.DatePopService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RetrofitViewModel : ViewModel() {
	
	private val MULTIPART_FORM = "multipart/form-data".toMediaTypeOrNull()
	private val IMAGE_PARAM = "image"
	
	fun uploadImageFromRetrofit(uri: Uri, contentResolver: ContentResolver) = viewModelScope.launch {
		val service: DatePopService = RetrofitClient.provideService("https://staging.datepop.co.kr/")
		
		val file = File(getPath(uri, contentResolver))
		val requestBody = RequestBody.create(MULTIPART_FORM, file)
		val body = MultipartBody.Part.createFormData("files", file.name, requestBody)
		val json = MultipartBody.Part.createFormData("json", "{“comment” : “kk”,“shop_id” : 1101,“latitude”: 37.4826301,“longitude”: 126.8944957,“num_adapter” : 2,“manager” : “a”,“radius” : [-38],“mac_address” : 6553665536}")
		runCatching {
			service.registerBeacon(listOf(body, json))
		}.onSuccess {
			println("Success:$it")
		}.onFailure {
			println("Failure:${it.message}")
		}
	}
	
	private fun getPath(
		uri: Uri?,
		contentResolver: ContentResolver
	): String {
		val projection = arrayOf(MediaStore.Images.Media.DATA)
		
		val cursor =
			contentResolver.query(uri ?: Uri.parse(""), projection, null, null, null) ?: return ""
		val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
		cursor.moveToFirst()
		val s = cursor.getString(column_index)
		cursor.close()
		return s
	}
}
package com.study.network.retrofit

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.study.network.databinding.ActivityRetrofitBinding
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine

class RetrofitActivity : AppCompatActivity() {
	
	private val binding: ActivityRetrofitBinding by lazy {
		ActivityRetrofitBinding.inflate(layoutInflater)
	}
	
	private val viewModel: RetrofitViewModel by viewModels()
	
	lateinit var uri: Uri
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		
		
		binding.selectImage.setOnClickListener {
			Matisse.from(this)
				.choose(MimeType.ofAll())
				.countable(true)
				.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
				.thumbnailScale(0.85f)
				.imageEngine(GlideEngine())
				.maxSelectable(4)
				.showPreview(false) // Default is `true`
				.forResult(0)
		}
		
		binding.uploadImage.setOnClickListener {
			viewModel.uploadImageFromRetrofit(uri, contentResolver)
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
				
				Glide.with(binding.selectedImage)
					.load(uri)
					.into(binding.selectedImage)
			}
		}
	}
}
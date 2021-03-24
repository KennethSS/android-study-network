package com.study.network

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.study.network.http.NetworkService
import com.study.network.retrofit.RetrofitClient

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkService().getNetwork("https://mobile-coding-test.gangnamunni.com/users:443")
    }
}
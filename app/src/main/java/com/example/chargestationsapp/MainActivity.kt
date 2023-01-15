package com.example.chargestationsapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.SettingsClient


class MainActivity : AppCompatActivity() {
    val TAG = "Permission"
 /*   private val mSettingsClient: SettingsClient by lazy {
        LocationServices.getSettingsClient(context)
    }

  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




    }



}
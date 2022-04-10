package com.linbug.speedmeter

import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.linbug.speedmeter.databinding.ActivityMainBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(), LocationListener {
    private val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private lateinit var binding: ActivityMainBinding
    private var lastLocation: Location? = null
    private var permissionGranted = false
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPermission()

        binding.viewSpeed.onSpeedChange(0f)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
        } else {
            permissionToast()
        }
    }


    private fun initPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    private fun permissionToast() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "please give fine location permission", Toast.LENGTH_SHORT).show()
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(this, "please give coarse location permission", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLocationChanged(location: Location) {
        var deltaTime = 0f
        var speed = 0f

        lastLocation?.let {
            deltaTime = (location.time - it.time) / 1000f
            speed = it.distanceTo(location) / deltaTime
        }
        lastLocation = location

        Log.d("Lindbug", speed.toString())
        binding.viewSpeed.onSpeedChange(speed)
    }
}
package com.example.scanify

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.scanify.databinding.CameraPageBinding
import com.budiyev.android.codescanner.CodeScanner

private const val CAMERA_REQUEST_CODE = 101

class Scan : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: CameraPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CameraPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermissions()
        codeScanner()
    }

    private fun codeScanner(){

        val scannerView = binding.scannerView
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val barcode = it.text
                    val intent = Intent(this@Scan, ManualScan::class.java)
                    intent.putExtra("barcode", barcode)
                    if (barcode.length != 12){
                        val btnCapture = binding.btnCapture
                        btnCapture.text = "UPC-12 barcode required"
                    }else{
                        startActivity(intent)
                    }
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialization error: ${it.message}")
                }
            }
        }

        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need camera permission", Toast.LENGTH_SHORT).show()
                } else {
                    //success
                }
            }
        }
    }
}
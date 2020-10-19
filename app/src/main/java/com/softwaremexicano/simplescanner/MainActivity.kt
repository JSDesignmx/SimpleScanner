package com.softwaremexicano.simplescanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val upc = 1
    private val code = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            startCam()
        }
    }

    private fun startCam() {
        if (checkPermission())
            startActivityForResult(Intent(this, ScannerDialog::class.java), upc)
        else
            requestPermission()
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            code
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            code -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(Intent(this, ScannerDialog::class.java), upc)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED
                        ) {
                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(getString(R.string.no_camera_access))
                            builder.setMessage(getString(R.string.allow_camera))
                            builder.setPositiveButton(getString(R.string.allow_permission)) { _, _ -> requestPermission() }
                            builder.setNegativeButton(getString(R.string.cancel)) { _, _ ->
                                textView.text = getString(R.string.no_camera_access)
                            }

                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == upc) textView.text = data?.data.toString()
    }
}
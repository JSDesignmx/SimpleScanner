package com.softwaremexicano.simplescanner

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.vision.CameraSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_scanner_dialog.*

class ScannerDialog : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner_dialog)
        title = ""
        buttonCancel.setOnClickListener {
            returnResult("Accion cancelada", false)
        }
    }

    override fun onStart() {
        super.onStart()
        disposable = barcodeView
            .drawOverlay()
            .setBeepSound(true)
            .setVibration(500)
            .setAutoFocus(true)
            .setFlash(false)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .getObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    returnResult(it.displayValue, true)
                },
                {
                    returnResult(it.message, false)
                })
    }

    override fun onBackPressed() {
        returnResult("Accion cancelada", false)
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    private fun returnResult(result: String?, b: Boolean) {
        val data = Intent()
        data.data = Uri.parse(result)
        if(b) setResult(Activity.RESULT_OK, data) else setResult(Activity.RESULT_CANCELED, data)
        finish()
    }
}
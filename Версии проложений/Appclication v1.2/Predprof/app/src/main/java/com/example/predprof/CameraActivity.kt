package com.example.predprof

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.predprof.retrofit_products.start.StartViewModel
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "CameraX"
        private const val FILE_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PERMISSION_CODE = 10
        private val PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var outputDir: File
    private var imageCupture: ImageCapture? = null
    lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

        if (allPermissionGrated()) {
            startCamera()
        }
        else {
            ActivityCompat.requestPermissions(
                this, PERMISSION, PERMISSION_CODE
            )
        }

        recognize.setOnClickListener {
            takePhoto()
        }

        cancel.setOnClickListener {
            goToMainActivity()
        }

        outputDir = getOutputDir()
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun takePhoto() {
        val imageCapture = imageCupture?:return

        val photoFile = File(outputDir,
        SimpleDateFormat(FILE_FORMAT, Locale.US)
            .format(System.currentTimeMillis()) + ".jpg")


        val outputOption = ImageCapture.OutputFileOptions
            .Builder(photoFile).build()
        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(baseContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: OutputFileResults) {
                    val uri = Uri.fromFile(photoFile)
                    val msg = "Photo : $uri"
                    viewModel.pushMyImage(photoFile)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(baseContext, "Save error : ${exception.message}", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener( Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(preview.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageCupture = ImageCapture
                .Builder()
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCupture
                )
            } catch (e: Exception) {
                Log.e(TAG, "Bind error" ,e)
            }
        },
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun getOutputDir(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.absoluteFile.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdir()
            }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir
        else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun allPermissionGrated() = PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {
            if (allPermissionGrated()) {
                startCamera()
            }
            else {
                Toast.makeText(this, "Permission error", Toast.LENGTH_SHORT).show()
                goToMainActivity()
            }
        }
    }
}
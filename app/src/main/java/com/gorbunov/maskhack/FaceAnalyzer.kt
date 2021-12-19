package com.gorbunov.maskhack

import android.annotation.SuppressLint
import android.content.Context
import android.media.ImageReader
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceAnalyzer(val facesState: SnapshotStateList<Face>, val viewModel: MainViewModel) : ImageAnalysis.Analyzer {

    private val realTimeOpts = FaceDetectorOptions.Builder()
//        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .setMinFaceSize(0.20f)
//        .enableTracking()
        .build()

    private val detector = FaceDetection.getClient(realTimeOpts)




    @SuppressLint("UnsafeOptInUsageError")
    override fun
            analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let {
            val inputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    if (faces.size > 0){
                        facesState.clear()
                        facesState.addAll(faces)
                        viewModel.faces.postValue(faces)
                        viewModel.previewWidth = inputImage.width
                        viewModel.previewHeight = inputImage.height
                    }
                    imageProxy.close()
                }
                .addOnFailureListener {
                    imageProxy.close()
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
                .addOnCanceledListener{
                    imageProxy.close()
                }
        }
    }
}
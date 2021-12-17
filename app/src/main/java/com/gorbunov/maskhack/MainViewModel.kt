package com.gorbunov.maskhack

import android.graphics.PointF
import android.hardware.camera2.CameraCharacteristics
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.face.Face

class MainViewModel: ViewModel() {
    val faces = MutableLiveData<List<Face>>()
    val cameraState = MutableLiveData(CameraCharacteristics.LENS_FACING_FRONT)
}
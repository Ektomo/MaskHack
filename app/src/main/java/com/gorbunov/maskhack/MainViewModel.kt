package com.gorbunov.maskhack

import android.graphics.PointF
import android.hardware.camera2.CameraCharacteristics
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.face.Face

class MainViewModel : ViewModel() {
    val faces = MutableLiveData<List<Face>>()
    val cameraState = MutableLiveData(CameraCharacteristics.LENS_FACING_FRONT)

    var previewWidth: Int = 0
    var previewHeight: Int = 0


}

val imageModels = listOf(
    ImageModel(
        path = R.drawable.carnival_mask,
        hasNose = false,
        hasEye = HasEye(has = true, isPair = true),
        hasEar = HasEar(has = false, isPair = false),
        hasMouth = false,
        hasTop = false,
        hasBottom = false
    ),
    ImageModel(
        path = R.drawable.bear_nose_ears,
        hasNose = true,
        hasEye = HasEye(has = false, isPair = false),
        hasEar = HasEar(has = true, isPair = true),
        hasMouth = false,
        hasTop = true,
        hasBottom = false
    ),
    ImageModel(
        path = R.drawable.deer_face,
        hasNose = true,
        hasEye = HasEye(has = false, isPair = false),
        hasEar = HasEar(has = true, isPair = true),
        hasMouth = false,
        hasTop = true,
        hasBottom = false
    ),
    ImageModel(
        path = R.drawable.deer_horns,
        hasNose = false,
        hasEye = HasEye(has = false, isPair = false),
        hasEar = HasEar(has = true, isPair = true),
        hasMouth = false,
        hasTop = true,
        hasBottom = false
    ),
    ImageModel(
        path = R.drawable.irish_hat,
        hasNose = false,
        hasEye = HasEye(has = true, isPair = true),
        hasEar = HasEar(has = false, isPair = false),
        hasMouth = false,
        hasTop = true,
        hasBottom = false
    ),
    ImageModel(
        path = R.drawable.shower_hat,
        hasNose = false,
        hasEye = HasEye(has = false, isPair = false),
        hasEar = HasEar(has = false, isPair = false),
        hasMouth = false,
        hasTop = true,
        hasBottom = false
    )
)
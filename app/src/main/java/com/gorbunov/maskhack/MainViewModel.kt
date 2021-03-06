package com.gorbunov.maskhack

import android.graphics.PointF
import android.hardware.camera2.CameraCharacteristics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.face.Face


/**В данном примере viewModel используется только для передачи набора объектов,
 *  для этого мог подойти любой pojo class, но если грузить информацию из сети
 *  или делать сложные расчета неотносящиеся к вью, я бы использовал эти функции здесь,
 *  подписываясь на результат и обновляя compose view
*/
class MainViewModel : ViewModel() {
    val faces = MutableLiveData<MutableList<Face>>()
    val cameraState = MutableLiveData(CameraCharacteristics.LENS_FACING_FRONT)

    var previewWidth: Int = 0
    var previewHeight: Int = 0


}

/**
 * Список заранее подготовленных моделей,
 *      достаточно использовать несколько главных параметров, на основе которых можно сделать
 *      алгоритм рассчета местоположения
 */
val imageModels = listOf(
    ImageModel(
        path = R.drawable.carnival_mask,
        hasNose = false,
        hasEye = HasEye(has = true, left = true, right = true),
        hasEar = HasEar(has = false, left = false, right = false),
        hasMouth = false,
        hasTop = false,
        hasBottom = false
    ),
    ImageModel(
        path = R.drawable.bear_nose_ears,
        hasNose = true,
        hasEye = HasEye(has = false,  left = false, right = false),
        hasEar = HasEar(has = true, left = true, right = true),
        hasMouth = false,
        hasTop = true,
        hasBottom = false,
        requireSizePlus = 60.dp
    ),
    ImageModel(
        path = R.drawable.deer_face,
        hasNose = true,
        hasEye = HasEye(has = false, left = false, right = false),
        hasEar = HasEar(has = true, left = true, right = true),
        hasMouth = false,
        hasTop = true,
        hasBottom = false,
        requireSizePlus = 80.dp
    ),
    ImageModel(
        path = R.drawable.deer_horns,
        hasNose = false,
        hasEye = HasEye(has = false, left = false, right = false),
        hasEar = HasEar(has = false, left = false, right = false),
        hasMouth = false,
        hasTop = true,
        hasBottom = false,
        requireSizePlus = 300.dp
    ),
    ImageModel(
        path = R.drawable.irish_hat,
        hasNose = false,
        hasEye = HasEye(has = true, left = true, right = true),
        hasEar = HasEar(has = false, left = false, right = false),
        hasMouth = false,
        hasTop = true,
        hasBottom = false,
        requireSizePlus = 50.dp
    ),
    ImageModel(
        path = R.drawable.shower_hat,
        hasNose = false,
        hasEye = HasEye(has = false, left = false, right = false),
        hasEar = HasEar(has = false, left = false, right = false),
        hasMouth = false,
        hasTop = true,
        hasBottom = false,
        requireSizePlus = 50.dp
    )
)
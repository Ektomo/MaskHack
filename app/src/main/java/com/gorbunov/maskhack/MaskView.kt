package com.gorbunov.maskhack

import android.hardware.camera2.CameraCharacteristics
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import android.util.DisplayMetrics
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer


//Отрисовка маски
@Composable
fun MaskView(
    face: Face,
    facing: MutableState<Int>,
    viewModel: MainViewModel,
    imageModel: ImageModel
) {
//    val contour = face.allContours

    val orientation = LocalConfiguration.current.orientation
    var widthScaleFactor = 1f
    var heightScaleFactor = 1f
//    val landmarks = face.allLandmarks

    val previewWidth: Int
    val previewHeight: Int
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        previewWidth = viewModel.previewWidth
        previewHeight = viewModel.previewHeight
    } else {
        previewWidth = viewModel.previewHeight
        previewHeight = viewModel.previewWidth
    }


//    val minX = contour.minOfOrNull { cont -> (cont.points.minOfOrNull { it.x } ?: 0.0f) }
//    val maxX = contour.maxOfOrNull { cont -> (cont.points.maxOfOrNull { it.x } ?: 0.0f) }
//    val minY = contour.minOfOrNull { cont -> (cont.points.minOfOrNull { it.y } ?: 0.0f) }
//    val maxY = contour.maxOfOrNull { cont -> (cont.points.maxOfOrNull { it.y } ?: 0.0f) }

    val configuration = LocalConfiguration.current
    val density = configuration.densityDpi
//    val screenHeightDp = configuration.screenHeightDp
//
//    val screenWidthDp = configuration.screenWidthDp
    val screenHeight = convertDpToPixel(configuration.screenHeightDp.toFloat(), density)

    val screenWidth = convertDpToPixel(configuration.screenWidthDp.toFloat(), density)

    widthScaleFactor = screenWidth / previewWidth
    heightScaleFactor = screenHeight / previewHeight


    val degreeZ = face.headEulerAngleZ
    val degreeX = face.headEulerAngleX
    val degreeY = face.headEulerAngleY

//    DisposableEffect(key1 = viewModel.faces){
//        onDispose {
//            viewModel.faces.value?.clear()
//        }
//    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
//            .height((maxHeightM * 1.5).dp)
    ) {

        widthScaleFactor = screenWidth / previewWidth
        heightScaleFactor = screenHeight / previewHeight

        val rightEarXPx = translateX(
            (face.getLandmark(FaceLandmark.RIGHT_EAR)?.position?.x) ?: 0.0f,
            widthScaleFactor
        )

        val rightEarX =
            convertPixelsToDp(
                rightEarXPx, density
            )

//        val rightEarY =
//            translateY(
//                (face.getLandmark(FaceLandmark.RIGHT_EAR)?.position?.y) ?: 0.0f,
//                heightScaleFactor
//            )

        val leftEyeYPx = translateY(
            face.getLandmark(
                FaceLandmark.LEFT_EYE
            )?.position?.y ?: 0.0f, heightScaleFactor
        )
        val leftEyeY = convertPixelsToDp(
            leftEyeYPx, density
        )

        val rightEyeYPx = translateY(
            face.getLandmark(
                FaceLandmark.RIGHT_EYE
            )?.position?.y ?: 0.0f, heightScaleFactor
        )

        val leftEarXPx = translateX(
            (face.getLandmark(FaceLandmark.LEFT_EAR)?.position?.x ?: 0.0f),

            widthScaleFactor
        )

        val leftEarYPx = translateX(
            (face.getLandmark(FaceLandmark.LEFT_EAR)?.position?.y ?: 0.0f),

            heightScaleFactor
        )

        val rightEarYPx = translateX(
            (face.getLandmark(FaceLandmark.RIGHT_EAR)?.position?.y ?: 0.0f),

            heightScaleFactor
        )
        val leftEarX =
            convertPixelsToDp(leftEarXPx, density)
        val imgWidth =
            getImageWidth(imageModel, rightEarX, leftEarX)

        val mouthBottomYPx =
            translateY(
                face.getLandmark(FaceLandmark.MOUTH_BOTTOM)?.position?.y ?: 0.0f,
                heightScaleFactor
            )


        val noseYPx = translateY(
            face.getLandmark(FaceLandmark.NOSE_BASE)?.position?.y ?: 0.0f,
            heightScaleFactor
        )

        val noseXPx = translateX(
            face.getLandmark(FaceLandmark.NOSE_BASE)?.position?.x ?: 0.0f,
            widthScaleFactor
        )

        val widthCenter = if (imageModel.hasNose){
            rightEarXPx
        }else{
            rightEarXPx
//            - imgWidth/2
        }

        val imgHeightPoint =
            getYCoordinate(
                imageModel,
                leftEyeYPx,
                rightEyeYPx,
                screenHeight,
                degreeX,
                mouthBottomYPx,
                noseYPx,
                leftEarYPx,
                rightEarYPx,
                screenWidth - rightEarXPx,
            )



        //Чтобы прорисовать контур, расскоментировать блок ниже и в FaceAnalyzer подключить сканирование контура
//        contour.forEach { fc ->
//            fc.points.forEach { pf ->
//                Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
//                    drawCircle(
//                        Color.Magenta, 10f, Offset(
//                            screenWidth - translateX(pf.x, widthScaleFactor),
//                            translateY(pf.y, heightScaleFactor)
//                        )
//                    )
//                })
//            }
//        }




        //Основная отрисовка картинки
        Image(
            painter = painterResource(id = imageModel.path),
            contentDescription = "",
            modifier = Modifier
                .width(imgWidth.dp )
//                .rotate(degree)
                .graphicsLayer {
                    rotationX = degreeX
                    rotationY = -degreeY
                    rotationZ = degreeZ
                    translationX =
                        if (facing.value == CameraCharacteristics.LENS_FACING_FRONT) {
                            screenWidth - widthCenter - imageModel.requireSizePlus.value  - (face.headEulerAngleY * 4)
                        } else {
                            widthCenter - (face.headEulerAngleY * 4) - imageModel.requireSizePlus.value
                        }
                    translationY = imgHeightPoint - 150

                }
        )


    }




}


/**
 * Самое широкое место для ошибок, здесь очень очень очень грубые рассчеты для вертикального смещения
 *  Идея в том, что мы просто идем от верхнего элемента(можно от нижнего) и сужаем все варианты
 *  таким образом в конце концов мы находим расстояние на которое надо сместить маску по вертикали.
 *  Это писалось ночью и тут допущена масса ошибок, которые я не успел поправить, а после бессоной ночи
 *      голова соображает туговато)
 */
@Composable
private fun getYCoordinate(
    imageModel: ImageModel,
    leftEyeYPx: Float,
    rightEyeYPx: Float,
    screenHeight: Float,
    degreeX: Float,
    mouthBottomYPx: Float,
    noseYPx: Float,
    leftEarYPx: Float,
    rightEarYPx: Float,
    someX: Float,
) = when {
    imageModel.hasTop -> {
        val topY = ((leftEyeYPx + rightEyeYPx) / 2) - screenHeight / 6 + degreeX * 8
//            val bottomY = mouthBottomYPx + screenHeight/8
//            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
//                drawCircle(
//                    Color.Magenta, 8f, Offset(
//                        someX,
//                        topY
//                    )
//                )
//            })
        when {
            imageModel.hasBottom -> {
                val bottomY = mouthBottomYPx + screenHeight / 8
                ((topY + bottomY) / 2)
            }
            imageModel.hasMouth -> {
                ((topY + mouthBottomYPx) / 2) - 100
            }
            imageModel.hasNose -> {
                ((topY + noseYPx) / 2) - 100
            }
            imageModel.hasEye.has -> {
                when {
                    imageModel.hasEye.left && !imageModel.hasEye.right -> {
                        ((topY + leftEyeYPx) / 2) - 200

                    }
                    imageModel.hasEye.right && !imageModel.hasEye.left -> {
                        ((topY + rightEyeYPx) / 2) - 200

                    }
                    else -> {
                        ((topY + (rightEyeYPx + leftEyeYPx) / 2) / 2) - 200

                    }
                }

            }
            imageModel.hasEar.has -> {
                when {
                    imageModel.hasEar.left -> {
                        ((topY + leftEarYPx) / 2)

                    }
                    imageModel.hasEar.right -> {
                        ((topY + rightEarYPx) / 2)

                    }
                    else -> {
                        ((topY + (rightEarYPx + rightEarYPx) / 2) / 2)

                    }
                }
            }
            else -> {
                topY - 100

            }
        }

    }
    imageModel.hasEye.has
//    && (leftEyeYPx < leftEarYPx || leftEyeYPx < rightEarYPx || rightEyeYPx < leftEarYPx || rightEyeYPx < rightEarYPx)
    -> {
        val topY = ((leftEyeYPx + rightEyeYPx) / 2)
        when {
            imageModel.hasBottom -> {
                val bottomY = mouthBottomYPx + screenHeight / 8
                ((topY + bottomY) / 2)

            }
            imageModel.hasMouth -> {
                ((topY + mouthBottomYPx) / 2)

            }
            imageModel.hasNose -> {
                ((topY + noseYPx) / 2)

            }
            imageModel.hasEar.has -> {
                when {
                    imageModel.hasEar.left -> {
                        ((topY + leftEarYPx) / 2)

                    }
                    imageModel.hasEar.right -> {
                        ((topY + rightEarYPx) / 2)

                    }
                    else -> {
                        ((topY + (rightEarYPx + rightEarYPx) / 2) / 2)

                    }
                }
            }
            else -> {
                topY

            }
        }
    }
    imageModel.hasEar.has
//    && (leftEyeYPx < leftEarYPx || leftEyeYPx < rightEarYPx || rightEyeYPx < leftEarYPx || rightEyeYPx < rightEarYPx)
    -> {
        val topY = ((leftEarYPx + rightEarYPx) / 2)
        when {
            imageModel.hasBottom -> {
                val bottomY = mouthBottomYPx + screenHeight / 8
                ((topY + bottomY) / 2)

            }
            imageModel.hasMouth -> {
                ((topY + mouthBottomYPx) / 2)

            }
            imageModel.hasNose -> {
                ((topY + noseYPx) / 2)

            }
            imageModel.hasEye.has -> {
                when {
                    imageModel.hasEye.left -> {
                        ((topY + leftEyeYPx) / 2)

                    }
                    imageModel.hasEye.right -> {
                        ((topY + rightEyeYPx) / 2)

                    }
                    else -> {
                        ((topY + (rightEyeYPx + leftEyeYPx) / 2) / 2)

                    }
                }

            }
            else -> {
                topY

            }
        }
    }
    imageModel.hasNose -> {
        when {
            imageModel.hasBottom -> {
                val bottomY = mouthBottomYPx + screenHeight / 8
                ((noseYPx + bottomY) / 2)

            }
            imageModel.hasMouth -> {
                ((noseYPx + mouthBottomYPx) / 2)

            }
            else -> {
                noseYPx

            }
        }
    }
    imageModel.hasMouth -> {
        when {
            imageModel.hasBottom -> {
                val bottomY = mouthBottomYPx + screenHeight / 8
                ((mouthBottomYPx + bottomY) / 2)

            }
            else -> {
                mouthBottomYPx

            }
        }
    }
    else -> {
        mouthBottomYPx + screenHeight / 8
    }
}


//Получить ширину картинки в зависимости от наличие ушей или рогов
private fun getImageWidth(
    imageModel: ImageModel,
    rightEarX: Float,
    leftEarX: Float,
) =
//    if (imageModel.hasEar.has) {
//    if (imageModel.hasEar.left && imageModel.hasEar.right) {
//        (rightEarX - leftEarX) + 100
//    } else {
//        (rightEarX - leftEarX) + 50
//    }
//} else {
    (rightEarX - leftEarX) +  imageModel.requireSizePlus.value
//}


//Учесть размеры экрана
fun translateX(horizontal: Float, widthScaleFactor: Float): Float {
    return horizontal * widthScaleFactor
}

//Учесть размеры экрана
fun translateY(vertical: Float, heightScaleFactor: Float): Float {
    return vertical * heightScaleFactor
}


//Перевести dpi в пиксели
fun convertDpToPixel(dp: Float, densityDp: Int): Float {
    return dp * (densityDp.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

//Перевести пиксели в dpi
fun convertPixelsToDp(px: Float, densityDp: Int): Float {
    return px / (densityDp.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}


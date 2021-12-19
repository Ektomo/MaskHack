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
    val contour = face.allContours

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

        val mouthBottomYPx = translateY(
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


//        var leftEarY =
//            convertPixelsToDp(
//                translateY(
//                    (face.getLandmark(FaceLandmark.LEFT_EAR)?.position?.y) ?: 0.0f,
//                    heightScaleFactor
//                ), density
//            )


//        ((maxX ?: 0f) - (minX ?: 0f)) / 2
//        if (facing.value == CameraCharacteristics.LENS_FACING_BACK) rightEarX - leftEarX else leftEarX - rightEarX
//    val widthCenter = (leftEarX + imgWidth / 2)

//        val heightCenter =
//            (leftEyeY)


//        contour.forEach { fc ->
//            fc.points.forEach { pf ->
//                Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
//                    drawCircle(
//                        Color.Magenta, 8f, Offset(
//                            screenWidth - translateX(pf.x, widthScaleFactor),
//                            translateY(pf.y, heightScaleFactor)
//                        )
//                    )
//                })
//            }
//        }
//
//        landmarks.forEach { fc ->
////            fc.points.forEach { pf ->
//            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
//
//                val x = maxWidthM - translateX(fc.position.x, widthScaleFactor)
//                val y = translateY(fc.position.y, heightScaleFactor)
//                if (fc.landmarkType == FaceLandmark.LEFT_EAR) {
////                    length = fc.position.length()/1000
//                    println("")
//                }
//                drawCircle(
//                    Color.Cyan, 12f, Offset(
//                        x,
//                        y
//                    )
//                )
//            })
////            }
//        }
//        val imageBitmap = ImageBitmap.imageResource(id = R.drawable.carnival_mask)
//        Canvas(modifier = Modifier.width(imgWidth.dp).rotate(degree), onDraw = {
//            drawImage(imageBitmap)
//        })


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
                            screenWidth - widthCenter  - (face.headEulerAngleY * 4)
                        } else {
                            widthCenter - (face.headEulerAngleY * 4)
                        }
                    translationY = imgHeightPoint - 150

                }
//                .align(BiasAlignment(0f, 0f))
//                .padding(start = )
//                .offset {
//                    IntOffset(leftEarX.toInt(), heightCenter.toInt())
//
//                }
//                .align { size, space, layoutDirection ->
//                    val centerX = widthCenter
//                    val h = rightEarX
//                    val centerY = leftEyeY * 1.1
//                    val resolvedHorizontalBias = if (layoutDirection == LayoutDirection.Ltr) {
//                        0.3
//                    } else {
//                        -1 * 0.3
//                    }
//
//                    val x = centerX
//                    val y = centerY
//                    IntOffset(x.roundToInt(), y.roundToInt())
//                }
        )

//        val x = translateX(face.boundingBox.centerX().toFloat(), facing.value, maxWidthM, widthScaleFactor)
//        val y = translateY(face.boundingBox.centerY().toFloat(), heightScaleFactor)
//        val xOffset = scaleX(face.boundingBox.width().toFloat() , widthScaleFactor)
//        val yOffset = scaleY(face.boundingBox.height().toFloat() , heightScaleFactor)
//        val left = x - xOffset
//        val top = y - yOffset
//        val right = x + xOffset
//        val bottom = y + yOffset
//
//        Canvas(modifier = Modifier
//            .fillMaxWidth(0.3f)
//            .fillMaxHeight(0.3f), onDraw = {
//            drawRect(Color.White, Offset(left, top), Size(face.boundingBox.width().toFloat(), face.boundingBox.height().toFloat()))
//        })

    }




}


//Рассчитать координать
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
                ((topY + mouthBottomYPx) / 2)
            }
            imageModel.hasNose -> {
                ((topY + noseYPx) / 2)
            }
            imageModel.hasEye.has -> {
                when {
                    imageModel.hasEye.left && !imageModel.hasEye.right -> {
                        ((topY + leftEyeYPx) / 2)

                    }
                    imageModel.hasEye.right && !imageModel.hasEye.left -> {
                        ((topY + rightEyeYPx) / 2)

                    }
                    else -> {
                        ((topY + (rightEyeYPx + leftEyeYPx) / 2) / 2) - 300

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
                topY - 150

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
    (rightEarX - leftEarX)
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


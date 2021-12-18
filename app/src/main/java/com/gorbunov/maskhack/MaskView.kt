package com.gorbunov.maskhack

import android.R.attr
import android.graphics.PointF
import android.hardware.camera2.CameraCharacteristics
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceLandmark
import android.R.attr.y

import android.R.attr.x
import androidx.compose.ui.geometry.Size
import android.R.attr.right

import android.R.attr.left
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt
import android.util.DisplayMetrics
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer


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
        val leftEarX =
            convertPixelsToDp(leftEarXPx, density)
        val imgWidth =
            getImageWidth(imageModel, rightEarX, leftEarX)

        val mouthBottomYPx = translateY(
            face.getLandmark(FaceLandmark.MOUTH_BOTTOM)?.position?.y ?: 0.0f,
            heightScaleFactor
        )


        val imgHeightPoint: Int

        if (imageModel.hasTop) {
            val top = ((leftEyeYPx + rightEyeYPx) / 2) - screenHeight/5 + degreeX
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawCircle(
                    Color.Cyan, 12f, Offset(
                        screenWidth - leftEarXPx,
                        top
                    )
                )
            })
            if (imageModel.hasBottom) {

            }
        }


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
            painter = painterResource(id = R.drawable.carnival_mask),
            contentDescription = "",
            modifier = Modifier
                .width(imgWidth.dp)
//                .rotate(degree)
                .graphicsLayer {
                    rotationX = degreeX
                    rotationY = -degreeY
                    rotationZ = degreeZ
                    translationX = if (facing.value == CameraCharacteristics.LENS_FACING_FRONT) {
                        screenWidth - rightEarXPx - (face.headEulerAngleY * 4)
                    } else {
                        rightEarXPx - (face.headEulerAngleY * 4)
                    }
                    translationY = leftEyeYPx - 220
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


//Получить ширину картинки в зависимости от наличие ушей или рогов
private fun getImageWidth(
    imageModel: ImageModel,
    rightEarX: Float,
    leftEarX: Float
) = if (imageModel.hasEar.has) {
    if (imageModel.hasEar.isPair) {
        (rightEarX - leftEarX) + 100
    } else {
        (rightEarX - leftEarX) + 50
    }
} else {
    (rightEarX - leftEarX)
}


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


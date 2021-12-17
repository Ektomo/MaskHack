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
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceLandmark
import android.R.attr.y

import android.R.attr.x
import androidx.compose.ui.geometry.Size
import android.R.attr.right

import android.R.attr.left
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt


@Composable
fun MaskView(face: Face, facing: MutableState<Int>, maxWidthM: Float, maxHeightM: Float) {
    val contour = face.allContours

    var widthScaleFactor = 1.0f
    var heightScaleFactor = 1.0f
    val landmarks = face.allLandmarks

    val imgWidth =
        ((face.getLandmark(FaceLandmark.RIGHT_EYE)?.position?.x) ?: 0.0f) - (face.getLandmark(
            FaceLandmark.LEFT_EYE
        )?.position?.x ?: 0.0f)
    val imgHeight = face.boundingBox.height()
    val degree = face.headEulerAngleZ


    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val a = ""




    BoxWithConstraints(
        modifier = Modifier
            .width(imgWidth.dp)
            .height(imgHeight.dp)
    ) {

        widthScaleFactor = 1.5f
        heightScaleFactor = 1.5f

        contour.forEach { fc ->
            fc.points.forEach { pf ->
                Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                    drawCircle(
                        Color.Magenta, 8f, Offset(
                            translateX(pf.x, facing.value, maxWidthM, widthScaleFactor),
                            translateY(pf.y, heightScaleFactor)
                        )
                    )
                })
            }
        }

        landmarks.forEach { fc ->
//            fc.points.forEach { pf ->
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawCircle(
                    Color.Cyan, 12f, Offset(
                        translateX(fc.position.x, facing.value, maxWidthM, widthScaleFactor),
                        translateY(fc.position.y, heightScaleFactor)
                    )
                )
//                drawImage(R.drawable.carnival_mask)
            })
//            }
        }

        Image(
            painter = painterResource(id = R.drawable.carnival_mask),
            contentDescription = "",
            modifier = Modifier
                .width(imgWidth.dp)
                .rotate(degree)
                .align { size, space, layoutDirection ->
                    val centerX = (space.width - size.width).toFloat() / 2f
                    val centerY = (space.height - size.height).toFloat() / 2f
                    val resolvedHorizontalBias = if (layoutDirection == LayoutDirection.Ltr) {
                        1.0
                    } else {
                        -1 * 1.0
                    }

                    val x = centerX * (1 + resolvedHorizontalBias)
                    val y = centerY * (1 + 1.0)
                    IntOffset(x.roundToInt(), y.roundToInt())
                }
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


fun translateX(x: Float, type: Int, maxWidth: Float, widthScaleFactor: Float): Float {
    return if (type == CameraCharacteristics.LENS_FACING_FRONT) {
        maxWidth - scaleX(x - 475, widthScaleFactor)
    } else {
        scaleX(x, widthScaleFactor)
    }
}

fun translateY(y: Float, heightScaleFactor: Float): Float {
    return scaleY(y - 20, heightScaleFactor)
}


fun scaleX(horizontal: Float, widthScaleFactor: Float): Float {
    return horizontal * widthScaleFactor
}


fun scaleY(vertical: Float, heightScaleFactor: Float): Float {
    return vertical * heightScaleFactor
}


//
//List<FaceLandmark> landmarks = face.getAllLandmarks();
//for (FaceLandmark landmark : landmarks) {
////      for (PointF point : faceContour.getPoints()) {
//    float px = translateX (landmark.getPosition().x);
//    float py = translateY (landmark.getPosition().y);
//    canvas.drawCircle(px, py, FACE_POSITION_RADIUS, landmarkPositionPaint);
////      }
//}



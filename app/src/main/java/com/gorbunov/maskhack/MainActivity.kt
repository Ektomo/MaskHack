package com.gorbunov.maskhack

import android.Manifest
import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.face.Face
import com.gorbunov.maskhack.ui.theme.MaskHackTheme
import kotlin.math.roundToInt

@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaskHackTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CheckPermissionScreen(viewModel)
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun CheckPermissionScreen(viewModel: MainViewModel) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    PermissionRequired(
        permissionState = cameraPermissionState,
        permissionNotGrantedContent = {
            LaunchedEffect(Unit) {
                cameraPermissionState.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            Column {
                Text(
                    "Camera permission denied."
                )
            }
        }
    ) {
        FaceRecognitionScreenContent(viewModel)
    }
}

@Composable
fun FaceRecognitionScreenContent(viewModel: MainViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val selector = remember {
        mutableStateOf(CameraSelector.LENS_FACING_FRONT)
    }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val faces = remember {
        mutableStateListOf<Face>()
    }


//    Scaffold {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.matchParentSize(),
            factory = { ctx ->

                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                cameraProviderFuture.addListener({

                    val cameraProvider = cameraProviderFuture.get()
                    val preview = androidx.camera.core.Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(selector.value)
                        .build()

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetResolution(Size(maxWidth.value.toInt(), maxHeight.value.toInt()))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setImageQueueDepth(10)
                        .build()
                        .apply {
                            setAnalyzer(executor, FaceAnalyzer(faces, viewModel))
                        }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                }, executor)

                previewView
            },
        )

//            faces.value?.forEach {face ->
//                for (i in 1..(face.allContours.size)){
//                    if (face.getContour(i)?.points != null){
//                        SinusPlotter(face = face.getContour(i)!!.points, color = colors[i], maxHeight, maxWidth)
//                    }
//                }
//            }

        faces.forEach { face ->
            MaskView(
                face = face,
                facing = selector,
                viewModel,
                imageModels[5]
            )
        }
//        AndroidView(factory = { ctx ->
//            mGraphicOverlay = GraphicOverlay(ctx, null)
//            mGraphicOverlay!!.setCameraInfo(
//                (maxWidth.value * 2).roundToInt(),
//                (maxHeight.value * 2).roundToInt(),
//                selector.value
//            )
//
//            mGraphicOverlay!!
//        })
//
////            if (faces.value != null){
////                drawContur(faces = faces.value!!)
////            }
//
//
//        DisposableEffect(key1 = faces.value) {
//            if (faces.value != null && mGraphicOverlay != null) {
//                faces.value!!.forEach { face ->
//                    mGraphicOverlay!!.clear()
//                    val f: Face = face
//                    val faceContourGraphic = FaceContourGraphic(mGraphicOverlay)
//                    mGraphicOverlay!!.add(faceContourGraphic)
//                    faceContourGraphic.updateFace(f)
//                }
//
//            } else if (faces.value != null && faces.value!!.isEmpty()) {
//                mGraphicOverlay?.clear()
//            }
//
//            onDispose { mGraphicOverlay?.clear() }
//
//        }


    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaskHackTheme {
        Greeting("Android")
    }
}
package tw.camera.opencvjetpackcompose.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.util.Size
import androidx.camera.core.*
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import tw.camera.opencvjetpackcompose.camera.ui.BlurDetectionText
import tw.camera.opencvjetpackcompose.camera.ui.CameraPreview
import tw.camera.opencvjetpackcompose.camera.ui.ImageCaptureButton
import tw.camera.opencvjetpackcompose.utils.Permission
import java.io.File


@ExperimentalPermissionsApi
@Composable
fun CameraModule(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Permission(
        permission = Manifest.permission.CAMERA,
        permissionNotAvailableContent = {
            Column(modifier) {
                Text("NoCamera!")
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                        )
                    }
                ) {
                    Text("open Settings")
                }
            }
        },
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
    var imageAnalysisUseCase by remember { mutableStateOf<UseCase>(ImageAnalysis.Builder().build())}
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    val coroutineScope = rememberCoroutineScope()
    val imageCaptureUseCase by remember {
        mutableStateOf(
            ImageCapture.Builder()
                .apply {
                    setJpegQuality(95)
                    setTargetAspectRatio(RATIO_4_3)
//                    setTargetResolution(Size(3120, 4160))  //XperiaAce2
                    setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                }
                .build()
        )
    }

    Box {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onUseCase = {
                previewUseCase = it
            }
        )
        BlurDetectionText(
            modifier = Modifier
                .width(290.dp)
                .padding(16.dp)
                .background(Color.White),
            onUseCase = {
                imageAnalysisUseCase = it
            }
        )
        ImageCaptureButton(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                Log.d("CameraModule", "ImageCaptureButton onClick")
                coroutineScope.launch {
                    imageCaptureUseCase.takePicture(context)
                }
            }
        )
    }
    LaunchedEffect(previewUseCase) {
        val cameraProvider = context.getCameraProvider()
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase, imageAnalysisUseCase
            )
        } catch (ex: Exception) {
            Log.e("CameraCapture", "Failed to bind camera use cases", ex)
        }
    }
}
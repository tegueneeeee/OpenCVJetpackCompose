package tw.camera.opencvjetpackcompose.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.camera.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
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
        Row {
            BlurDetectionText(
                modifier = Modifier
                    .width(290.dp)
                    .padding(16.dp),
                onUseCase = {
                    imageAnalysisUseCase = it
                }
            )
            //        GyroScopeText(
            //            modifier = Modifier
            //                .width(200.dp)
            //                .padding(16.dp),
            //        )
            ImageCaptureButton(
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                onClick = {

                }
            )
        }
        ImageCaptureButton(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            onClick = {
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
package tw.camera.opencvjetpackcompose.camera.ui

import android.util.Size
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tw.camera.opencvjetpackcompose.camera.analysis.BlurDetection
import tw.camera.opencvjetpackcompose.camera.executor


@Composable
fun BlurDetectionText(
    modifier: Modifier = Modifier,
    onUseCase: (UseCase) -> Unit = { }
) {
    var blurScore by remember { mutableStateOf("") }
    val context = LocalContext.current
    Text(
        text = blurScore,
        modifier = modifier
    )
    onUseCase(
        ImageAnalysis.Builder()
            .apply {
                setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            }
            .build()
            .also {
                it.setAnalyzer(context.executor, BlurDetection { blur ->
                    blurScore = String.format("blurScore: %.5f", blur)
                })
            }
    )
}


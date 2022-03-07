package tw.camera.opencvjetpackcompose.camera.ui

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tw.camera.opencvjetpackcompose.camera.analysis.BlurDetection
import tw.camera.opencvjetpackcompose.camera.executor
import java.nio.ByteBuffer


@Composable
fun BlurDetectionText(
    modifier: Modifier = Modifier,
    onUseCase: (UseCase) -> Unit = { }
) {
    var blurScore by remember { mutableStateOf("") }
    var luminosity by remember { mutableStateOf("") }
    val context = LocalContext.current
    Text(
        text = blurScore,
        modifier = modifier
    )
    Text(
        text = luminosity,
        modifier = modifier
    )
    onUseCase(
        ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(context.executor, BlurDetection { blur ->
                    blurScore = String.format("blurScore: %.5f", blur)
                })
            }
    )
}

private fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}



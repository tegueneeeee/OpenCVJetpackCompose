package tw.camera.opencvjetpackcompose.camera.ui

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tw.camera.opencvjetpackcompose.camera.analsis.BlurDetection
import tw.camera.opencvjetpackcompose.camera.executor


@Composable
fun BlurDetectionText(
    modifier: Modifier = Modifier,
    onUseCase: (UseCase) -> Unit = { }
) {
    var blurScore by remember { mutableStateOf("") }
    val context = LocalContext.current
    Text(text = blurScore, modifier = modifier)
    onUseCase(
        ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(context.executor, BlurDetection { blur ->
                    Log.d("BlurDetectionText", "Average luminosity: $blur")
                    blurScore = String.format("BlurScore: %.5f", blur)
                })
            }
    )
}



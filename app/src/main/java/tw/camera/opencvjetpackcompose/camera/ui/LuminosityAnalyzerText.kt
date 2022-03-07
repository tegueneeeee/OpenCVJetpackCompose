package tw.camera.opencvjetpackcompose.camera.ui

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCase
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tw.camera.opencvjetpackcompose.camera.analysis.LuminosityAnalyzer
import tw.camera.opencvjetpackcompose.camera.executor

@Composable
fun LuminosityAnalyzerText(
    modifier: Modifier = Modifier,
    onUseCase: (UseCase) -> Unit = { }
) {
    var luminosity by remember { mutableStateOf("") }
    val context = LocalContext.current
    Text(
        text = luminosity,
        modifier = modifier
    )
    onUseCase(
        ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(context.executor, LuminosityAnalyzer { luma ->
                    luminosity = String.format("Luminosity: %.5f", luma)
                })
            }
    )
}
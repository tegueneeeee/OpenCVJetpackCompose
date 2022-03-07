package tw.camera.opencvjetpackcompose.camera.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ImageCaptureButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color = if (isPressed) Color.Gray else Color.White
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(2.dp, Color.Black),
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = color)
    ) {
    }
}

@Preview
@Composable
fun ImageCaptureButtonPreview() {
    ImageCaptureButton(
        modifier = Modifier.
                size(100.dp)
    )
}
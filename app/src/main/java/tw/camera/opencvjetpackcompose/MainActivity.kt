package tw.camera.opencvjetpackcompose

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import tw.camera.opencvjetpackcompose.camera.CameraModule
import tw.camera.opencvjetpackcompose.camera.gallery.GallerySelect
import tw.camera.opencvjetpackcompose.camera.ui.CameraPreview
import tw.camera.opencvjetpackcompose.camera.getCameraProvider
import tw.camera.opencvjetpackcompose.ui.theme.OpenCVJetpackComposeTheme
import tw.camera.opencvjetpackcompose.utils.Permission

class MainActivity : ComponentActivity() {

    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenCVJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainContext(Modifier.fillMaxSize())
                }
            }
        }
    }
    /**
     * A native method that is implemented by the 'opencvjetpackcompose' native library,
     * which is packaged with this application.
     */
    companion object {
        private const val TAG = "MainActivity"
    }
}
val EMPTY_IMAGE_URI: Uri = Uri.parse("file//dev/null")

@ExperimentalPermissionsApi
@Composable
fun MainContext(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CameraModule(
            modifier = modifier
        )
    }
}


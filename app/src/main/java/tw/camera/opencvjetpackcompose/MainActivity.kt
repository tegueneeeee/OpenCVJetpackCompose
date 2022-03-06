package tw.camera.opencvjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import tw.camera.opencvjetpackcompose.ui.theme.OpenCVJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenCVJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    (Modifier.fillMaxSize())
                }
            }
        }
    }

    /**
     * A native method that is implemented by the 'opencvjetpackcompose' native library,
     * which is packaged with this application.
     */

    companion object {
        // Used to load the 'opencvjetpackcompose' library on application startup.
        init {
            System.loadLibrary("opencvjetpackcompose")
        }
    }
}
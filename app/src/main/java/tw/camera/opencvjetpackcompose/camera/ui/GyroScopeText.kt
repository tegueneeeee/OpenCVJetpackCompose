package tw.camera.opencvjetpackcompose.camera.ui



import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import tw.camera.opencvjetpackcompose.camera.analysis.GyroScope

@Composable
fun GyroScopeText(
    modifier: Modifier = Modifier
) {
    var gyroValue by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sensorManager: SensorManager? = getSystemService(context, SensorManager::class.java)
    val gyroSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    Text(
        text = gyroValue,
        modifier = modifier
    )
    sensorManager?.registerListener(GyroScope{ gyro ->
        gyroValue = gyro
    }, gyroSensor, SensorManager.SENSOR_DELAY_GAME)
}

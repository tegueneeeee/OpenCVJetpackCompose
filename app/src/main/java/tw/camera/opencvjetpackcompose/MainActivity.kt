package tw.camera.opencvjetpackcompose

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import tw.camera.opencvjetpackcompose.camera.CameraModule
import tw.camera.opencvjetpackcompose.ui.theme.OpenCVJetpackComposeTheme
import java.lang.String

class MainActivity : ComponentActivity() {

    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        setContent {
            OpenCVJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainContext(Modifier.fillMaxSize())
                }
            }
        }

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mGyroListener = GyroscopeListner()

        gyroInit()
        mSensorManager.registerListener(mGyroListener, mGyroSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onResume() {
        gyroInit()
        super.onResume()
        Log.d(TAG, "onResume()")
        mSensorManager.registerListener(mGyroListener, mGyroSensor, SensorManager.SENSOR_DELAY_UI)
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
        mSensorManager.unregisterListener(mGyroListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
        mSensorManager.unregisterListener(mGyroListener)
    }

    private fun gyroInit() {
        pitch = 0.0
        roll = 0.0
        yaw = 0.0
    }

    private class GyroscopeListner: SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            val gyroX = event!!.values[0]
            val gyroY = event!!.values[1]
            val gyroZ = event!!.values[2]

            dt = (event.timestamp - timestamp) * NS2S
            timestamp = event.timestamp.toDouble()

            if (dt - timestamp * NS2S != 0.0) {
                pitch += gyroX * dt
                roll += gyroY * dt
                yaw += gyroZ * dt

                Log.d(TAG, "GYROSCOPE           [X]:" + String.format("%.4f", gyroX)
                        + "           [Y]:" + String.format("%.4f", gyroY)
                        + "           [Z]:" + String.format("%.4f", gyroZ)
                        + "           [Pitch]: " + String.format("%.1f", pitch * RAD2DGR)
                        + "           [Roll]: " + String.format("%.1f", roll * RAD2DGR)
                        + "           [Yaw]: " + String.format("%.1f", yaw * RAD2DGR)
                        + "           [dt]: " + String.format("%.4f", dt)
                )
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        }

    }
    companion object {
        private const val TAG = "MainActivity"

        private lateinit var mSensorManager: SensorManager
        private lateinit var mGyroListener: SensorEventListener
        private lateinit var mGyroSensor: Sensor

        private var pitch = 0.0
        private var roll = 0.0
        private var yaw = 0.0

        private const val RAD2DGR = 180 / Math.PI
        private const val NS2S = 1.0f / 1000000000.0f

        private var timestamp = 0.0
        private var dt = 0.0
    }

}

@ExperimentalPermissionsApi
@Composable
fun MainContext(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CameraModule(
            modifier = modifier
        )
    }
}


package tw.camera.opencvjetpackcompose.camera.analysis

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

typealias GyroListener = (gyro: DoubleArray) -> Unit

class GyroScope(private val listener: GyroListener): SensorEventListener {
    private val gyroArray = DoubleArray(4) { 0.0 }
    private var pitch = 0.0
    private var roll = 0.0
    private var yaw = 0.0

    private var timestamp = 0.0
    private var dt = 0.0

    override fun onSensorChanged(event: SensorEvent) {
        val gyroX = event.values[0].toDouble()
        val gyroY = event.values[1].toDouble()
        val gyroZ = event.values[2].toDouble()

        dt = (event.timestamp - timestamp) * NS2S
        timestamp = event.timestamp.toDouble()

        if (dt - timestamp * NS2S != 0.0) {
            pitch += gyroY * dt
            roll += gyroX * dt
            yaw += gyroZ * dt

            gyroArray[0] = pitch * RAD2DGR
            gyroArray[1] = roll * RAD2DGR
            gyroArray[3] = yaw * RAD2DGR
            gyroArray[4] = dt

            listener(gyroArray)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    companion object {
        private const val RAD2DGR = 180 / Math.PI
        private const val NS2S = 1.0f / 1000000000.0f
    }
}
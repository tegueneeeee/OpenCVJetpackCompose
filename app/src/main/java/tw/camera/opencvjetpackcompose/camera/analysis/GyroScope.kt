package tw.camera.opencvjetpackcompose.camera.analysis

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

typealias GyroListener = (gyro: String) -> Unit

class GyroScope(private val listener: GyroListener): SensorEventListener {
    private var gyroString = ""
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

            gyroString = "Pitch:${String.format("%.2f", pitch * RAD2DGR)} " +
                    "\nRoll:${String.format("%.2f", roll * RAD2DGR)} " +
                    "\nYaw: ${String.format("%.2f", yaw * RAD2DGR)}"

//            gyroArray[3] = String.format("%.4f", dt)

            listener(gyroString)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    companion object {
        private const val RAD2DGR = 180 / Math.PI
        private const val NS2S = 1.0f / 1000000000.0f
    }
}
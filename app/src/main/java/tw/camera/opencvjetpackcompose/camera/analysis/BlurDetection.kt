package tw.camera.opencvjetpackcompose.camera.analysis

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.CvType.CV_8UC1
import org.opencv.core.CvType.CV_8UC4
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.nio.ByteBuffer

typealias BlurListener = (blur: Double) -> Unit

class BlurDetection(private val listener: BlurListener) : ImageAnalysis.Analyzer {

    private external fun blurDetectionOpenCV(matAddrInput: Long): Double
    private external fun convertRGBtoGray(matAddrInput: Long, matAddrResult: Long)

    override fun analyze(image: ImageProxy) {
        matInput = Mat(image.height, image.width, CV_8UC4, image.planes[0].buffer)
        matResult = Mat(matInput.rows(), matInput.cols(), matInput.type())
        convertRGBtoGray(matInput.nativeObjAddr, matResult.nativeObjAddr)
        listener(blurDetectionOpenCV(matResult.nativeObjAddr))

        Log.i(TAG, "[analyze] width = ${image.width} " +
                "height = ${image.height} " +
                "Rotation = ${image.imageInfo.rotationDegrees}")
        Log.i(TAG, "[analyze] mat width = ${matInput.cols()}" +
                " mat height = ${matInput.rows()} mat type ${matInput.type()}")

        image.close()
    }

    companion object {
        private lateinit var matInput: Mat
        private lateinit var matResult: Mat
        private const val TAG = "BlurDetection"

        init {
            System.loadLibrary("opencv_java4")
            System.loadLibrary("native-lib")
        }
    }
}

fun convertYUV2RGB(image: ImageProxy): Mat {
    var nv21: ByteArray

    val yBuffer: ByteBuffer = image.planes[0].buffer
    val uBuffer: ByteBuffer = image.planes[1].buffer
    val vBuffer: ByteBuffer = image.planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuv = Mat(image.height + image.height / 2, image.width, CvType.CV_8UC1)
    yuv.put(0, 0, nv21)
    val rgb = Mat()
    Imgproc.cvtColor(yuv, rgb, Imgproc.COLOR_YUV2RGB_NV21, 3)
    Core.rotate(rgb, rgb, Core.ROTATE_90_CLOCKWISE)
    return rgb
}


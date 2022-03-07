#include <jni.h>
#include <opencv2/opencv.hpp>

#define BLOCK 60

using namespace cv;

extern "C"
JNIEXPORT jdouble JNICALL
Java_tw_camera_opencvjetpackcompose_camera_analsis_BlurDetection_blurDetectionOpenCV(JNIEnv *env,
                                                                                     jobject thiz,
                                                                                     jlong mat_addr_input) {
    Mat &frame = *(Mat *)mat_addr_input;
    int cx = frame.cols / 2;
    int cy = frame.rows / 2;

    Mat fImage;
    frame.convertTo(fImage, CV_32F);

    Mat fourierTransform;
    dft(fImage, fourierTransform, DFT_SCALE|DFT_COMPLEX_OUTPUT);

    Mat q0(fourierTransform, Rect(0, 0, cx, cy));
    Mat q1(fourierTransform, Rect(cx, 0, cx, cy));
    Mat q2(fourierTransform, Rect(0, cy, cx, cy));
    Mat q3(fourierTransform, Rect(cx, cy, cx, cy));

    Mat tmp;
    q0.copyTo(tmp);
    q3.copyTo(q0);
    tmp.copyTo(q3);

    q1.copyTo(tmp);
    q2.copyTo(q1);
    tmp.copyTo(q2);

    fourierTransform(Rect(cx-BLOCK, cy-BLOCK, 2 * BLOCK, 2 * BLOCK)).setTo(0);

    Mat orgFFT;
    fourierTransform.copyTo(orgFFT);
    Mat p0(orgFFT, Rect(0, 0, cx, cy));
    Mat p1(orgFFT, Rect(cx, 0, cx, cy));
    Mat p2(orgFFT, Rect(0, cy, cx, cy));
    Mat p3(orgFFT, Rect(cx, cy, cx, cy));

    p0.copyTo(tmp);
    p3.copyTo(p0);
    tmp.copyTo(p3);

    p1.copyTo(tmp);
    p2.copyTo(p1);
    tmp.copyTo(p2);

    Mat invFFT;
    Mat logFFT;
    double minVal, maxVal;

    dft(orgFFT, invFFT, DFT_INVERSE | DFT_REAL_OUTPUT);

    invFFT = cv::abs(invFFT);
    cv::minMaxLoc(invFFT, &minVal, &maxVal, NULL, NULL);

    cv::log(invFFT, logFFT);
    logFFT *= 20;

    cv::Scalar result = cv::mean(logFFT);
    return result.val[0];
}

extern "C"
JNIEXPORT void JNICALL
Java_tw_camera_opencvjetpackcompose_camera_analsis_BlurDetection_convertRGBtoGray(JNIEnv *env,
                                                                                  jobject thiz,
                                                                                  jlong mat_addr_input,
                                                                                  jlong mat_addr_result) {
    Mat &matInput = *(Mat *)mat_addr_input;
    Mat &matResult = *(Mat *)mat_addr_result;

    cvtColor(matInput, matResult, COLOR_RGBA2GRAY);
}
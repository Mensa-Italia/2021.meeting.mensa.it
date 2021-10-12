package swaix.dev.mensaeventi.utils

import android.graphics.Bitmap
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageProxy
import com.google.mlkit.common.MlKitException
import java.nio.ByteBuffer

interface VisionImageProcessor {

//    fun processBitmap(bitmap: Bitmap?, graphicOverlay: GraphicOverlay?)
//
//    @Throws(MlKitException::class)
//    fun processByteBuffer(
//        data: ByteBuffer?, frameMetadata: FrameMetadata?, graphicOverlay: GraphicOverlay?
//    )
//
//    @RequiresApi(VERSION_CODES.KITKAT)
//    @Throws(MlKitException::class)
//    fun processImageProxy(image: ImageProxy?, graphicOverlay: GraphicOverlay?)

    /**
     * Stops the underlying machine learning model and release resources.
     */
    fun stop()
}

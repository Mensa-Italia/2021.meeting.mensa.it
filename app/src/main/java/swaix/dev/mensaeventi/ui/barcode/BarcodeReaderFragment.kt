package swaix.dev.mensaeventi.ui.barcode

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.BarcodeReaderFragmentBinding
import swaix.dev.mensaeventi.model.BarcodeModel
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.vibrate
import timber.log.Timber
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class BarcodeReaderFragment : BaseFragment() {


    lateinit var binding: BarcodeReaderFragmentBinding
    private val args: BarcodeReaderFragmentArgs by navArgs()
    private val cameraViewModel: CameraXViewModel by viewModels()


    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null

    private val screenAspectRatio: Int
        get() {
            // Get screen metrics used to setup camera for full screen resolution
            val metrics = DisplayMetrics().also { binding.viewFinder.display?.getRealMetrics(it) }
            return aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BarcodeReaderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @androidx.camera.core.ExperimentalGetImage
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        cameraViewModel.processCameraProvider.observe(viewLifecycleOwner, { _cameraProvider ->
            cameraProvider = _cameraProvider
            bindCameraUseCases()
        })
    }

    @androidx.camera.core.ExperimentalGetImage
    private fun bindCameraUseCases() {
        bindPreviewUseCase()
        bindAnalyseUseCase()
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        previewUseCase = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .build()
        previewUseCase!!.setSurfaceProvider(binding.viewFinder.surfaceProvider)

        try {
            cameraProvider!!.bindToLifecycle(
                /* lifecycleOwner= */this,
                cameraSelector!!,
                previewUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Timber.e(illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Timber.e(illegalArgumentException.message ?: "IllegalStateException")
        }
    }

    @androidx.camera.core.ExperimentalGetImage
    private fun bindAnalyseUseCase() {
        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .build()

        // Initialize our background executor
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(cameraExecutor, { imageProxy -> processImageProxy(barcodeScanner, imageProxy) })

        try {
            cameraProvider!!.bindToLifecycle(
                this,
                cameraSelector!!,
                analysisUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            Timber.e(illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Timber.e(illegalArgumentException.message ?: "IllegalStateException")
        }
    }

    var lastScan: BarcodeModel? = null
    var lastTime: Long = Long.MIN_VALUE


    @SuppressLint("UnsafeExperimentalUsageError")
    @androidx.camera.core.ExperimentalGetImage
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {

        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                with(binding) {

                    if (barcodes.any()) {
                        val barcode = barcodes.first()
                        Timber.d(barcode.rawValue)
                        val gson = Gson()
                        try {
                            val text = barcode.displayValue ?: ""
                            val parsed = gson.fromJson(text, BarcodeModel::class.java)
                            val t1 = lastScan
                            val t2 = lastTime + 5000
                            if (parsed.idEvent != args.event.id) {
                                val b = t1 == parsed
                                val b1 = t2 < Date().time
                                if (t1 == null || (b && b1)) {
                                    Toast.makeText(requireContext(), R.string.error_qr_code_read, Toast.LENGTH_LONG).show()
                                    root.vibrate()
                                    lastScan = parsed
                                    lastTime = Date().time
                                }
                            } else {
                                root.vibrate()
                                // Aggiungo questo perchè altrimenti a volte crasha...
                                lifecycleScope.launchWhenResumed {
                                    findNavController().navigate(BarcodeReaderFragmentDirections.actionBarcodeReaderFragmentToCheckInFragment(parsed.idEventQRCode))
                                }
                                lastScan = parsed
                                lastTime = Date().time
                            }
                        } catch (i: JsonSyntaxException) {
                            Toast.makeText(requireContext(), R.string.error_qr_code_json, Toast.LENGTH_LONG).show()
                            root.vibrate()
                        }

                    }


                }
            }
            .addOnFailureListener {
                Timber.e(it.message ?: it.toString())
            }.addOnCompleteListener {
                imageProxy.close()
            }
    }


    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}
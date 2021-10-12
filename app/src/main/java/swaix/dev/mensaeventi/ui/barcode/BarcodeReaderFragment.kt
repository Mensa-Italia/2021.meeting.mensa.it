package swaix.dev.mensaeventi.ui.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.databinding.BarcodeReaderFragmentBinding
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.ui.events.EventDetailFragment
import swaix.dev.mensaeventi.utils.VisionImageProcessor
import swaix.dev.mensaeventi.utils.barcode.CameraXViewModel

@AndroidEntryPoint
class BarcodeReaderFragment : BaseFragment() {

    companion object {

        private const val STATE_SELECTED_MODEL = "selected_model"
        private const val STATE_LENS_FACING = "lens_facing"
    }

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    lateinit var cameraSelector: CameraSelector
    private val cameraProvider: ProcessCameraProvider? = null
    private val previewUseCase: Preview? = null
    private val analysisUseCase: ImageAnalysis? = null
    private val imageProcessor: VisionImageProcessor? = null

    private val cameraViewModel: CameraXViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            lensFacing = savedInstanceState.getInt(STATE_LENS_FACING, CameraSelector.LENS_FACING_BACK)
        }
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return BarcodeReaderFragmentBinding.inflate(inflater, container, false).root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(BarcodeReaderFragmentBinding.bind(view)) {
            cameraViewModel.cameraProvider.observe(viewLifecycleOwner) {


            }

        }
    }
}
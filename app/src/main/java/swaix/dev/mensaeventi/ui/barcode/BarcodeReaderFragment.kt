package swaix.dev.mensaeventi.ui.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.databinding.BarcodeReaderFragmentBinding
import swaix.dev.mensaeventi.model.BarcodeModel
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.utils.vibrate
import uk.co.brightec.kbarcode.Barcode

@AndroidEntryPoint
class BarcodeReaderFragment : BaseFragment() {


    private val args: BarcodeReaderFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return BarcodeReaderFragmentBinding.inflate(inflater, container, false).root
    }

    var lastBarcodeRead: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(BarcodeReaderFragmentBinding.bind(view)) {
            lifecycle.addObserver(viewBarcode)

            viewBarcode.barcode.observe(viewLifecycleOwner, {
                val gson = Gson()
                try {
                    val parsed = gson.fromJson(it.displayValue, BarcodeModel::class.java)
                    if (parsed.idEvent.isNullOrEmpty()) {
                        showErrorMessage(it)
                    }else{
                        viewBarcode.vibrate()
                        findNavController().navigate(BarcodeReaderFragmentDirections.actionBarcodeReaderFragmentToCheckInFragment(parsed.idEvent.toInt()))
                    }
                } catch (i: JsonSyntaxException) {
                    showErrorMessage(it)
                }

                lastBarcodeRead = it.displayValue ?: ""
            })
        }
    }

    private fun BarcodeReaderFragmentBinding.showErrorMessage(it: Barcode) {
        if (lastBarcodeRead != it.displayValue) {
            Toast.makeText(requireContext(), "QR-Code non valido", Toast.LENGTH_LONG).show()
            viewBarcode.vibrate()
        }
    }


}
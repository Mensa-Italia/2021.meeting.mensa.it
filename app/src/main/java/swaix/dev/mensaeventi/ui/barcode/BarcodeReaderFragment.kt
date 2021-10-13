package swaix.dev.mensaeventi.ui.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import swaix.dev.mensaeventi.databinding.BarcodeReaderFragmentBinding
import swaix.dev.mensaeventi.ui.BaseFragment
import swaix.dev.mensaeventi.ui.events.EventDetailFragmentArgs

@AndroidEntryPoint
class BarcodeReaderFragment : BaseFragment() {


    private val args: BarcodeReaderFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return BarcodeReaderFragmentBinding.inflate(inflater, container, false).root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(BarcodeReaderFragmentBinding.bind(view)) {
            lifecycle.addObserver(viewBarcode)

            viewBarcode.barcodes.observe(viewLifecycleOwner, { barcodes ->
                if(barcodes.any()){
                    findNavController().navigate(BarcodeReaderFragmentDirections.actionBarcodeReaderFragmentToCheckInFragment(args.event))
                }
            })
        }
    }


}
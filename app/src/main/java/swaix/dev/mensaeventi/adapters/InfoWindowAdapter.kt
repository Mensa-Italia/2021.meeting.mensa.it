package swaix.dev.mensaeventi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.scopes.ActivityRetainedScoped
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.model.EventItem
import java.util.*

@ActivityRetainedScoped
class InfoWindowAdapter(private val context: Context, private val treeMap: TreeMap<String, EventItem>) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.info_window_marker, null, false)

        marker.snippet?.let{snippet ->

            val infoWindowData = treeMap.getValue(snippet) as? EventItem
//            view.title_marker.text = infoWindowData?.title ?: "Evento"
//
//            if (!infoWindowData?.showDateFrom.isNullOrEmpty()){
//                view.date_marker.text = formatDateWithSlash(infoWindowData!!.showDateFrom!!)
//                if (!infoWindowData.showDateTo.isNullOrEmpty()){
//                    view.date_marker.text = view.date_marker.text.toString() +" - "+ formatDateWithSlash(infoWindowData.showDateTo!!)
//                }
//            } else view.date_marker.text = ""
        }

        return view
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

}
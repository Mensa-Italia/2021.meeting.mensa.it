package swaix.dev.mensaeventi.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.scopes.ActivityRetainedScoped
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.model.EventItem
import swaix.dev.mensaeventi.ui.map.MapFragment
import java.util.*

@ActivityRetainedScoped
class InfoWindowUserAdapter(private val context: Context, private val treeMap: TreeMap<String, MapFragment.MyItem>) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.info_window_marker, null, false)

        marker.tag?.let{snippet ->
            val userItem = treeMap.getValue(snippet.toString()) as? MapFragment.MyItem
            view.findViewById<TextView>(R.id.title_marker).text = userItem?.title
        }

        return view
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

}
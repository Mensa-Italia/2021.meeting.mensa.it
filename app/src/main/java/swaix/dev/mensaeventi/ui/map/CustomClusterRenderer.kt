package swaix.dev.mensaeventi.ui.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import android.view.View
import android.widget.TextView
import swaix.dev.mensaeventi.R


open class CustomClusterRenderer(val mContext: Context, map: GoogleMap, clusterManager: ClusterManager<MapFragment.MyItem>) : DefaultClusterRenderer<MapFragment.MyItem>(mContext, map, clusterManager) {

    private var iconGenerator: IconGenerator? = null

    init {
        iconGenerator = IconGenerator(mContext.applicationContext)
    }

    override fun onBeforeClusterItemRendered(item: MapFragment.MyItem, markerOptions: MarkerOptions) {
        val markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
        markerOptions.icon(markerDescriptor).snippet(item.title)

        iconGenerator?.setBackground(null)
        val inflatedView: View = View.inflate(mContext, R.layout.marker_custom, null)
        inflatedView.findViewById<TextView>(R.id.initials_marker).text = item.getInitials()
        iconGenerator?.setContentView(inflatedView)

        markerOptions.icon(iconGenerator?.makeIcon()?.let { BitmapDescriptorFactory.fromBitmap(it)})

//        when (item?.type) {
//            "panchine" ->  markerOptions?.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_panchina))
//            "cartelloni" -> markerOptions?.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_cartellone))
//            "passicarrabili" -> markerOptions?.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_carraio))
//        }
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

//    override fun onBeforeClusterRendered(cluster: Cluster<PoiClusterItem>, markerOptions: MarkerOptions) {
//
//        mClusterIconGenerator?.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_cluster_circle))
//        mClusterIconGenerator?.setTextAppearance(R.style.custer_map_number_text)
//
//
//        val icon = mClusterIconGenerator!!.makeIcon(cluster.items.toString())
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
//    }

    override fun onClusterItemRendered(item: MapFragment.MyItem, marker: Marker) {
        //marker.tag = item.id_tag
        super.onClusterItemRendered(item, marker)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<MapFragment.MyItem>): Boolean {
        return cluster.size > 1 // if markers <=3 then not clustering
    }
}
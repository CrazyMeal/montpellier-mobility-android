package crazymeal.fr.montpelliermobility.map

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import crazymeal.fr.montpelliermobility.R
import kotlinx.android.synthetic.main.activity_map.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        map.isHorizontalMapRepetitionEnabled = false
        map.isVerticalMapRepetitionEnabled = false
        map.minZoomLevel = 14.0

        var boundingBox = BoundingBox(43.663857,3.944800,43.555588,3.809666)
        map.setScrollableAreaLimitDouble(boundingBox)


        val mapController = map.controller
        mapController.setZoom(19.0)
        val startPoint = GeoPoint(43.610053, 3.878702)
        mapController.setCenter(startPoint)

        var mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        mLocationOverlay.enableMyLocation()
        map.overlays.add(mLocationOverlay)



        //your items
        var items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Title", "Description", GeoPoint(43.608502,3.868672)))


        var itemGestureListener = object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                return true
            }

            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                return false
            }
        }

        //the overlay
        var mOverlay = ItemizedOverlayWithFocus<OverlayItem>(items, itemGestureListener, this)
        mOverlay.setFocusItemsOnTap(true);

        map.overlays.add(mOverlay);
    }
}

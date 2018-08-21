package crazymeal.fr.montpelliermobility.map

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import crazymeal.fr.montpelliermobility.R
import crazymeal.fr.montpelliermobility.parking.Parking
import kotlinx.android.synthetic.main.activity_map.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

private const val TAG_MAPVIEW_SETTINGS = "MAPVIEW_SETTING"

// Bounding box
private const val LATITUDE_NORTH_LIMIT = 43.663857
private const val LATITUDE_SOUTH_LIMIT = 43.555588
private const val LONGITUDE_EAST_LIMIT = 3.944800
private const val LONGITUDE_WEST_LIMIT = 3.809666

// Starting point
private const val LATITUDE_STARTING_POINT = 43.610053
private const val LONGITUDE_STARTING_POINT = 3.878702

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // We use the default tiles source
        map.setTileSource(TileSourceFactory.MAPNIK)

        // Activating zoom controls (specific for emulator)
        map.setBuiltInZoomControls(true)

        // Activating multi touch on map
        map.setMultiTouchControls(true)

        // Saving some space and tiles by not repeating the map
        map.isHorizontalMapRepetitionEnabled = false
        map.isVerticalMapRepetitionEnabled = false

        // Setting map min level
        map.minZoomLevel = 14.0

        this.setBoundingBoxToCity()
        this.activateLocationOverlay()
        //this.setInitialPositionOfView()

        //this.addParkingPointer()

        val parking: Parking = this.intent.extras.get("parking") as Parking
        this.setParkingInformationOnView(parking)
    }

    private fun setParkingInformationOnView(parking: Parking) {
        map_details_dynamic_parking_name.text = parking.name
        map_details_dynamic_occupied_places.text = parking.occupiedPlaces.toString()
        map_details_dynamic_total_places.text = parking.maxPlaces.toString()
        map_details_dynamic_progressBar.max = 100
        map_details_dynamic_progressBar.progress = parking.occupation

        map.controller.setZoom(19.0)
        Log.d(TAG_MAPVIEW_SETTINGS, "Creating point with latidude ${parking.latitude} and longitude ${parking.longitude}")
        val mStartPoint = GeoPoint(parking.latitude ?: LATITUDE_STARTING_POINT, parking.longitude ?: LONGITUDE_STARTING_POINT)
        map.controller.setCenter(mStartPoint)

        //your items
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Title", "Description", GeoPoint(parking.latitude ?: LATITUDE_STARTING_POINT, parking.longitude ?: LONGITUDE_STARTING_POINT)))
        val itemGestureListener = object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                return true
            }

            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                return false
            }
        }

        //the overlay
        val mOverlay = ItemizedOverlayWithFocus<OverlayItem>(items, itemGestureListener, this)
        mOverlay.setFocusItemsOnTap(true);

        map.overlays.add(mOverlay);
    }

    private fun setInitialPositionOfView() {
        map.controller.setZoom(19.0)
        val mStartPoint = GeoPoint(LATITUDE_STARTING_POINT, LONGITUDE_STARTING_POINT)
        map.controller.setCenter(mStartPoint)
    }

    private fun addParkingPointer() {
        //your items
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Title", "Description", GeoPoint(43.608502, 3.868672)))
        val itemGestureListener = object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                return true
            }

            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                return false
            }
        }

        //the overlay
        val mOverlay = ItemizedOverlayWithFocus<OverlayItem>(items, itemGestureListener, this)
        mOverlay.setFocusItemsOnTap(true);

        map.overlays.add(mOverlay);
    }

    /**
     * This method activate the display of current location of the user
     */
    private fun activateLocationOverlay() {
        Log.d(TAG_MAPVIEW_SETTINGS, "Activation user location display")

        val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        mLocationOverlay.enableMyLocation()
        map.overlays.add(mLocationOverlay)

        Log.d(TAG_MAPVIEW_SETTINGS, "User location display activated")
    }

    /**
     * This method limit the scrollable are to the wanted city (Montpellier)
     */
    private fun setBoundingBoxToCity() {
        Log.d(TAG_MAPVIEW_SETTINGS, "Setting a new bounding box for mapview")

        val mBoundingBox = BoundingBox(LATITUDE_NORTH_LIMIT, LONGITUDE_EAST_LIMIT, LATITUDE_SOUTH_LIMIT, LONGITUDE_WEST_LIMIT)
        map.setScrollableAreaLimitDouble(mBoundingBox)

        Log.d(TAG_MAPVIEW_SETTINGS, "Set bounding box for mapview $mBoundingBox")
    }
}

package crazymeal.fr.montpelliermobility

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import crazymeal.fr.montpelliermobility.map.MapFragment
import crazymeal.fr.montpelliermobility.parking.Parking
import crazymeal.fr.montpelliermobility.parking.ParkingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        ParkingFragment.OnListFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                this.loadBlankFragment()
            }
            R.id.nav_parking -> {
                this.loadParkingListFragment()
            }
            R.id.nav_map -> {
                this.loadMapFragment()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onListFragmentInteraction(parking: Parking?) {
        Log.d("FRAGMENT", "Triggered onListFragmentInteraction method with parking > ${parking ?: ""}")
    }

    override fun onFragmentInteraction(uri: Uri) {
        Log.d("MAP_FRAGMENT", "Triggered onFragmentInteraction method")
    }

    override fun onStart() {
        this.loadMapFragment()
        super.onStart()
    }

    private fun loadBlankFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
            .replace(R.id.layout_content, BlankFragment())
            .commit()
    }

    private fun loadParkingListFragment() {
        val parkingFragment = ParkingFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
            .replace(R.id.layout_content, parkingFragment)
            .commit()
    }

    private fun loadMapFragment() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.layout_content, MapFragment())
                .commit()
    }
}

package crazymeal.fr.montpelliermobility

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import crazymeal.fr.montpelliermobility.parking.Parking
import crazymeal.fr.montpelliermobility.parking.ParkingFragment
import crazymeal.fr.montpelliermobility.parking.ParkingScrapAsyncTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ParkingFragment.OnListFragmentInteractionListener {

    private val urlToGrab = listOf(
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_ANTI.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_ARCT.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_COME.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_CORU.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_EURO.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_FOCH.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GAMB.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GARE.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_TRIA.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_PITO.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_CIRC.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GARC.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_MOSS.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_SABI.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_SABL.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_STJ_SJLC.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_MEDC.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_OCCI.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_CAS_VICA.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GA109.xml",
        "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GA250.xml"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        this.loadParkingListFragment()
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
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onListFragmentInteraction(parking: Parking?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

        this.urlToGrab.forEach { url -> ParkingScrapAsyncTask(parkingFragment).execute(url) }
    }
}

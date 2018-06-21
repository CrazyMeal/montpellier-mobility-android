package crazymeal.fr.montpelliermobility

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.xml.parsers.DocumentBuilderFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ParkingFragment.OnListFragmentInteractionListener {

    private lateinit var recyclerViewParking: RecyclerView
    private lateinit var viewParkingAdapter: RecyclerView.Adapter<*>
    private lateinit var viewParkingManager: RecyclerView.LayoutManager

    private val dataset = listOf(
            Parking("Antigone", 10, 100),
            Parking("Comédie", 50, 80),
            Parking("Gaumont", 60, 120),
            Parking("Random1", 10, 190),
            Parking("Random2", 99, 120),
            Parking("Random3", 33, 95))

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
            R.id.nav_gallery -> {
                this.loadParkingListFragment()
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

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
            .replace(R.id.layout_content, BlankFragment())
            .commit()
    }

    private fun loadParkingListFragment() {
        val bundle = Bundle()
        bundle.putSerializable("parkingList", this.dataset.toTypedArray())

        val parkingFragment = ParkingFragment()
        parkingFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_content, parkingFragment)
            .commit()

        val urlString = "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_MOSS.xml"
        urlString!!.httpGet().responseObject(Parking.Deserializer()) { request, response, result ->
            when(result) {
                is Result.Failure -> {
                    Log.e("DOWNLOAD_ASYNC", result.getException().toString())
                }
                is Result.Success -> {
                    Log.i("DOWNLOAD_ASYNC", "Got some parking > " + result.component1().toString())
                    this.dataset.plus(result.component1())
                    parkingFragment.notifyAdapter()
                }
            }


        }
    }
}

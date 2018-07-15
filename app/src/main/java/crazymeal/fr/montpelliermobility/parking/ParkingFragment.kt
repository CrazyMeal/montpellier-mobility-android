package crazymeal.fr.montpelliermobility.parking

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import crazymeal.fr.montpelliermobility.R
import kotlinx.android.synthetic.main.fragment_parking_list.*

/**
 * A fragment representing a list of Parkings.
 * Activities containing this fragment MUST implement the
 * [ParkingFragment.OnListFragmentInteractionListener] interface.
 */
class ParkingFragment : Fragment() {

    private var columnCount = 1

    private var currentlyDownloadingParking: MutableList<String> = ArrayList()

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var listener: OnListFragmentInteractionListener

    private lateinit var parkingList: ArrayList<Parking>

    private val urlToScrap = mapOf(
            "ANTI" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_ANTI.xml",
            "ARCT" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_ARCT.xml",
            "COME" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_COME.xml",
            "CORU" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_CORU.xml",
            "EURO" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_EURO.xml",
            "FOCH" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_FOCH.xml",
            "GAMB" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GAMB.xml",
            "GARE" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GARE.xml",
            "Triangle" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_TRIA.xml",
            "Pitot" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_PITO.xml",
            "CIRC" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_CIRC.xml",
            "GARD" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GARC.xml",
            "MOSS" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_MOSS.xml",
            "SABI" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_SABI.xml",
            "SABL" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_SABL.xml",
            "SJLC" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_STJ_SJLC.xml",
            "MEDC" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_MEDC.xml",
            "OCCI" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_OCCI.xml",
            "VICA" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_CAS_VICA.xml",
            "GAUMONT-EST" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GA109.xml",
            "GAUMONT-OUEST" to "http://data.montpellier3m.fr/sites/default/files/ressources/FR_MTP_GA250.xml"
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_parking_list, container, false)

        if (view is SwipeRefreshLayout) {
            this.mSwipeRefreshLayout = view
            this.mSwipeRefreshLayout.isRefreshing = false
            this.mSwipeRefreshLayout.setOnRefreshListener { this.loadParkingDatas() }
        }

        return this.mSwipeRefreshLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(this.list) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        parkingList = ArrayList()
        this.list.adapter = ParkingFragmentAdapter(parkingList, listener)
        this.loadParkingDatas()
    }

    fun notifyAdapter(parking: Parking) {
        with(this.parkingList) {
            removeIf { p -> p.technicalName == parking.technicalName }
            add(parking)
        }

        with(this.currentlyDownloadingParking) {
            Log.d("DOWNLOADING_QUEUE", "Removing technical id ${parking.technicalName} from downloading queue")
            remove(parking.technicalName)

            Log.d("DOWNLOADING_QUEUE", "Download queue now looks like: ${this}")
            if (isEmpty()) {
                Log.d("DOWNLOADING_QUEUE", "All URLs has been downloaded, setting refresh to false and notifying adapter")
                mSwipeRefreshLayout.isRefreshing = false
                list.adapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadParkingDatas() {
        Log.d("DOWNLOADING_QUEUE", "Setting refresh to true")
        this.mSwipeRefreshLayout.isRefreshing = true

        this.urlToScrap.forEach { id, url ->
            Log.d("DOWNLOADING_QUEUE", "Adding URL ${url} to queue with id ${id}")
            this.currentlyDownloadingParking.add(id)

            ParkingScrapAsyncTask(this).execute(url)
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(parking: Parking?)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance() =
                ParkingFragment()
                        .apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}

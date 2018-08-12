package crazymeal.fr.montpelliermobility.parking

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }

        this.setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.let {
            inflater.inflate(R.menu.menu_parking_list, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return when (item.itemId) {
                R.id.menu_refresh -> {
                    this.loadParkingDatas()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        return super.onOptionsItemSelected(item)
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

        this.resources.getStringArray(R.array.parking_ids).forEach { id ->
            val arrayId = this.resources.getIdentifier(id, "array", this.context!!.packageName)
            val parkingName = this.resources.getStringArray(arrayId)[0]
            val url = this.resources.getStringArray(arrayId)[1]

            Log.d("DOWNLOADING_QUEUE", "Adding URL $url to queue with id $id")
            this.currentlyDownloadingParking.add(id)

            ParkingScrapAsyncTask(this).execute(parkingName, url)
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

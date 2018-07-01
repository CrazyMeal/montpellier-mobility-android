package crazymeal.fr.montpelliermobility.parking

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import crazymeal.fr.montpelliermobility.R

/**
 * A fragment representing a list of Parkings.
 * Activities containing this fragment MUST implement the
 * [ParkingFragment.OnListFragmentInteractionListener] interface.
 */
class ParkingFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    private lateinit var parkingList: ArrayList<Parking>

    private lateinit var parkingView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//            columnCount = it.getInt(ARG_COLUMN_COUNT)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_parking_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                val mValues= arguments?.getSerializable("parkingList") as Array<Parking>?
                parkingList = mValues?.toList().orEmpty() as ArrayList
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.parkingView = view as RecyclerView
        this.parkingView.adapter = ParkingFragmentAdapter(parkingList, listener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun notifyAdapter(parking: Parking) {
        this.parkingList.add(parking)
        this.parkingView.adapter.notifyDataSetChanged()
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
//                        .apply {
//                    arguments = Bundle().apply {
//                        putInt(ARG_COLUMN_COUNT, columnCount)
//                    }
//                }
    }
}

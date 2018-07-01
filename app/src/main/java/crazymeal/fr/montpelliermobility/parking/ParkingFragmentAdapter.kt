package crazymeal.fr.montpelliermobility.parking

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import crazymeal.fr.montpelliermobility.R


import crazymeal.fr.montpelliermobility.parking.ParkingFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_parking.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ParkingFragmentAdapter(
        private val mValues: List<Parking>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<ParkingFragmentAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Parking
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_parking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parking = mValues[position]
        holder.mParkingNameView.text = parking.name
        holder.mFreePlacesView.text = parking.freePlaces.toString()
        holder.mParkingOccupationBar.max = 100
        holder.mParkingOccupationBar.progress = parking.occupation

        with(holder.mView) {
            tag = parking
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mFreePlacesView: TextView = mView.text_view_parking_free_places
        val mParkingNameView: TextView = mView.text_view_parking_name
        val mParkingOccupationBar: ProgressBar = mView.progressBar

        override fun toString(): String {
            return super.toString() + " '" + mParkingNameView.text + "'"
        }
    }
}

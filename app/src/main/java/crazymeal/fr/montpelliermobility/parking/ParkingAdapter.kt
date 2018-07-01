package crazymeal.fr.montpelliermobility.parking

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import crazymeal.fr.montpelliermobility.R

class ParkingAdapter(private val dataset: Array<Parking>): RecyclerView.Adapter<ParkingAdapter.ViewHolder>() {

    class ViewHolder(val parkingView: View) : RecyclerView.ViewHolder(parkingView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val parkingItemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_parking, parent, false) as View
        return ViewHolder(parkingItemView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return dataset.size
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parkingName = holder.parkingView.findViewById<TextView>(R.id.text_view_parking_name)
        parkingName.text = dataset[position].name

        val parkingFreePlaces = holder.parkingView.findViewById<TextView>(R.id.text_view_parking_free_places)
        parkingFreePlaces.text = dataset[position].freePlaces.toString() + "/" + dataset[position].maxPlaces.toString()

        val progressBar = holder.parkingView.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.progress = dataset[position].occupation
        progressBar.max = 100
    }
}
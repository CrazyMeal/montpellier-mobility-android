package crazymeal.fr.montpelliermobility

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.security.SecureRandom

class ParkingAdapter(private val dataset: Array<String>): RecyclerView.Adapter<ParkingAdapter.ViewHolder>() {

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
        parkingName.text = dataset[position]

        val parkingFreePlaces = holder.parkingView.findViewById<TextView>(R.id.text_view_parking_free_places)
        parkingFreePlaces.text = SecureRandom().nextInt(500).toString()
    }
}
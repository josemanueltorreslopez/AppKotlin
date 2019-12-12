package apps.softtek.com.turisteando.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apps.softtek.com.turisteando.R
import apps.softtek.com.turisteando.models.Place
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton


class PlaceAdapter (var context: Context,
                    var places: List<Place>, var listener: OnPlaceSelected) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(places[position], listener)

        var url : String = places.get(position).PlacePhoto
        Glide.with(context!!).load(url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return places.size
    }

    interface OnPlaceSelected {
        fun onSelected(placeName: String, placeDescription: String, placePhoto: String)
    }


    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.place_image)

        fun bindItems(place: Place, listener: OnPlaceSelected) {
            val placeName = itemView.findViewById<TextView>(R.id.place_name)
            val placeDescription = itemView.findViewById<TextView>(R.id.place_description)
            val detailsButton = itemView.findViewById<MaterialButton>(R.id.detail_button)

            image = itemView.findViewById<ImageView>(R.id.place_image)

            placeName.text = place.PlaceName
            placeDescription.text = place.PlaceDescription

            detailsButton.setOnClickListener {
                listener.onSelected(place.PlaceName, place.PlaceDescription, place.PlacePhoto)
            }

        }
    }

}

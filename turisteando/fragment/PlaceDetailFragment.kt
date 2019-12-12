package apps.softtek.com.turisteando.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.softtek.com.turisteando.R
import apps.softtek.com.turisteando.models.Place
import apps.softtek.com.turisteando.models.Promo
import apps.softtek.com.turisteando.recycler.PromoAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_place_detail.*



class PlaceDetailFragment: BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_place_detail, container, false)
    }

    val promos = ArrayList<Promo>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var placeName = getArguments()!!.getString("placeName")
        var placeDescription = getArguments()!!.getString("placeDescription")
        var placePhoto = getArguments()!!.getString("placePhoto")
        val placeImageView = view!!.findViewById<ImageView>(R.id.place_image)


        place_name.text = placeName
        place_description.text = placeDescription
        Glide.with(context!!).load(placePhoto).into(placeImageView) //Carga de imagenes. Da problemas con los imagenView


        val recyclerView = view!!.findViewById<RecyclerView>(R.id.promos_recycler)

        recyclerView.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        val adapter = PromoAdapter(context!!,promos)
        recyclerView.adapter = adapter


        FirebaseDatabase.getInstance().reference.child("Promociones").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(e: DatabaseError) {
                Log.d(PlaceDetailFragment::class.java.simpleName, "${e.message} - ${e.toException()}")
            }

            override fun onDataChange(ds: DataSnapshot) {
                promos.clear()
                ds.children.forEach{ promosSnapshot->
                    val promo = promosSnapshot.getValue(Promo::class.java)
                    promo?.let {
                        var placeName = getArguments()!!.getString("placeName")
                        if (promo.PromoParent == placeName) {
                            promos.add(promo)
                        }

                    }
                }
                adapter.notifyDataSetChanged()
            }
        })

        promo_call_button.setOnClickListener{
            Toast.makeText(context, "Disabled", Toast.LENGTH_SHORT).show()
        }

        promo_agenda_button.setOnClickListener{
            Toast.makeText(context, "Disabled", Toast.LENGTH_SHORT).show()
        }

    }
}
package apps.softtek.com.turisteando.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.softtek.com.turisteando.R
import apps.softtek.com.turisteando.models.App
import apps.softtek.com.turisteando.models.Place
import apps.softtek.com.turisteando.recycler.PlaceAdapter
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.disposables.CompositeDisposable

class PlaceFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance(): PlaceFragment {


            var fragmentPlace = PlaceFragment()
            var args = Bundle()
            fragmentPlace.arguments = args
            return fragmentPlace
        }

    }

    //val places = ArrayList<Place>()

    private val disposable = CompositeDisposable()
    private val places = mutableListOf<Place>()
    private lateinit var adapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_place, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val recyclerView = view?.findViewById<RecyclerView>(R.id.place_recycler)
        val recyclerView = view.findViewById<RecyclerView>(R.id.place_recycler)
        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        //App.updatePlaces()

        adapter = PlaceAdapter(requireContext(), places, object : PlaceAdapter.OnPlaceSelected {
            override fun onSelected(placeName: String, placeDescription: String, placePhoto: String) {
                // Load BottomsheetFragment
                val bundle = Bundle()
                bundle.putString("placeName", placeName) //key and value
                bundle.putString("placeDescription", placeDescription)
                bundle.putString("placePhoto", placePhoto)
                val placeDetailFragment = PlaceDetailFragment()
                placeDetailFragment.setArguments(bundle)
                placeDetailFragment.show(requireFragmentManager(), placeDetailFragment.tag)
            }
        })

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter

        disposable.add((requireContext().applicationContext as App).getDestinationBus()
                .getChanges()
                .subscribe(this::updatePlaces)
        )


    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }

    private fun updatePlaces(destination: String) {
        FirebaseDatabase.getInstance().reference.child("Lugares").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(e: DatabaseError) {
                Log.d(DestinationFragment::class.java.simpleName, "${e.message} - ${e.toException()}")
            }

            override fun onDataChange(ds: DataSnapshot) {
                places.clear()
                ds.children.forEach{ destinoSnapshot->
                    val lugar = destinoSnapshot.getValue(Place::class.java)
                    lugar?.let {
                        if (lugar.PlaceParent == destination) {
                            places.add(lugar)
                        }

                    }
                }
                adapter.notifyDataSetChanged()
            }
        })

        val hideCardView = view!!.findViewById<MaterialCardView>(R.id.sun_card)
        val hideDestinationHint = view!!.findViewById<TextView>(R.id.destination_hint)

        hideCardView.setVisibility(View.GONE)
        hideDestinationHint.setVisibility(View.GONE)

    }

}

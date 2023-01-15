package com.example.chargestationsapp.ui.main.map

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.chargestationsapp.data.*
import com.example.chargestationsapp.databinding.FragmentMapBinding
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.*
import kotlinx.coroutines.*


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var huaweiMap: HuaweiMap
    private lateinit var marker: Marker
    private lateinit var cameraUpdate: CameraUpdate
    private lateinit var cameraPosition: CameraPosition
    private val MAP_BUNDLE_KEY = "MapBundleKey"

    private var title: String = ""
    private var phone: String? = null
    private var mail: String? = null
    private var website: String? = null
    private var latitude: Double = 41.031261
    private var longtitude: Double = 29.117277
    private var addressName: String? = null
    private var distance = 0.0
    private var countryCode = "TR"
    private var distanceunit = 2
    private var key = "?key=26018b6e-bf05-4e36-ba42-4ee689645f76"
    var markerList: ArrayList<MarkerOptions> = ArrayList()

    private lateinit var binding: FragmentMapBinding


    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        hasPermissions(requireContext())
        arguments?.let {
            latitude = MapFragmentArgs.fromBundle(it).latitude.toDouble()
            longtitude = MapFragmentArgs.fromBundle(it).longitude.toDouble()
            distance = MapFragmentArgs.fromBundle(it).distance.toDouble()
            countryCode = MapFragmentArgs.fromBundle(it).countryCode
        }

        var mapViewBundle: Bundle? = null

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY)
        }


        GlobalScope.launch {
            suspend {
                viewModel.getFromDataAPI(
                    key,
                    distanceunit,
                    countryCode,
                    latitude,
                    longtitude,
                    distance
                )
                withContext(Dispatchers.Main) {
                    binding.huaweiMapView.apply {
                        onCreate(mapViewBundle)
                        getMapAsync(this@MapFragment)
                    }
                }
            }.invoke()
        }
        observeLiveData()

    }


    private fun observeLiveData() {
        viewModel.stationList.observe(viewLifecycleOwner, Observer { stations ->
            stations.forEach { station ->
                var levelID = "1"
                station.Connections.forEach { item ->
                    levelID = item.LevelID.toString()
                }
                val loc = station.AddressInfo.Latitude
                val lng = station.AddressInfo.Longitude
                phone = station.AddressInfo.ContactTelephone1
                mail = station.AddressInfo.ContactEmail
                website = station.AddressInfo.RelatedURL
                addressName = station.AddressInfo.AddressLine1
                title = station.AddressInfo.Title
                val marker = MarkerOptions().position(LatLng(loc, lng)).anchor(
                    0.5f,
                    0.5f
                ).title(title).snippet(levelID)
                markerList.add(marker)
            }
        })

        viewModel.MapLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    binding.huaweiMapView.visibility = View.GONE
                }
            }
        })

        viewModel.ErrorAPI.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    binding.huaweiMapView.visibility = View.GONE
                    binding.maperror.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onMapReady(map: HuaweiMap) {
        huaweiMap = map

        var markerTıtle = ""

        markerList.forEach { marker ->
            if (marker.snippet == "3") {
                huaweiMap.addMarker(
                    marker.icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                    ).snippet("")
                )
            }
            if (marker.snippet == "2") {
                huaweiMap.addMarker(
                    marker.icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_YELLOW
                        )
                    ).snippet("")
                )
            }
        }


        marker = huaweiMap.addMarker(
            MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker())
                .title("Your Location")
                .position(LatLng(latitude, longtitude))
        )

        huaweiMap?.setOnMarkerClickListener { marker ->
            marker.showInfoWindow()
            markerTıtle = marker.title
            true
        }
        huaweiMap.setOnInfoWindowClickListener { click ->
            viewModel.stationList.observe(viewLifecycleOwner, Observer { markers ->
                val equipmentList = ArrayList<EquipmentItem>()
                markers.forEach { marker ->
                    if (markerTıtle == marker.AddressInfo.Title) {
                        marker.Connections.forEach { item ->
                            val equipmentItem = EquipmentItem(
                                amps = item.Amps,
                                voltage = item.Voltage,
                                powerKw = item.PowerKW,
                                title = item.ConnectionType.Title
                            )
                            equipmentList.add(equipmentItem)
                        }
                        val detail = Detail(
                            nearestAddress = marker.AddressInfo?.Title,
                            location = LatLng(
                                marker.AddressInfo.Latitude,
                                marker.AddressInfo.Longitude
                            ),
                            phone = marker.AddressInfo?.ContactTelephone1,
                            email = marker.AddressInfo?.ContactEmail,
                            website = marker.AddressInfo?.RelatedURL,
                            equipmentList = equipmentList
                        )
                        val action = MapFragmentDirections.actionMapFragmentToDetailFragment(detail)
                        view?.findNavController()?.navigate(action)
                    }


                }
            })
            true
        }

        cameraPosition = CameraPosition.builder()
            .target(LatLng(41.031261, 29.117277))
            .zoom(10f)
            .bearing(2.0f)
            .tilt(2.5f).build()
        cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        huaweiMap.moveCamera(cameraUpdate)


    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

}
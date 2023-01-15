package com.example.chargestationsapp.ui.main.search

import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.chargestationsapp.R
import com.example.chargestationsapp.databinding.FragmentSearchBinding
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.location.*
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService

class SearchFragment : Fragment() {

    private var mAuthManager: AccountAuthService? = null
    private var mAuthParam: AccountAuthParams? = null
    private var PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    private var PERMISSION_ALL = 1


    val TAG = "location"

    companion object {
        fun newInstance() = SearchFragment()
    }

    val placeList: MutableList<String> = mutableListOf(
        "",
        "TR",
        "UK"
    )

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel

    private lateinit var username: String

    val builder = LocationSettingsRequest.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "sdk < 28 Q")
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf<String>(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                )
                ActivityCompat.requestPermissions(requireActivity(), strings, 1)
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireContext(),
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(requireActivity(), strings, 2)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION successful")
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION  failed")
            }
        }
        if (requestCode == 2) {
            if (grantResults.size > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(
                    TAG,
                    "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful"
                )
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions(PERMISSIONS, PERMISSION_ALL)
        arguments?.let {
            username = SearchFragmentArgs.fromBundle(it).username
        }
        binding.userText.text = "Hi," + username
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, placeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countryCodeSpinner.adapter = adapter
        binding.searchButton.setOnClickListener {
            val distance = binding.editTextTownName.text.toString()
            val latitude = binding.latitude.text.toString()
            val longitude = binding.longitude.text.toString()
            val spinnerCountryCode = binding.countryCodeSpinner.selectedItem.toString()
            if (distance != "" && latitude != "") {
                val action = SearchFragmentDirections.actionSearchFragmentToMapFragment(
                    distance.toInt(),
                    spinnerCountryCode,
                    latitude,
                    longitude
                )
                view.findNavController().navigate(action)
            } else {
                Toast.makeText(
                    requireContext(),
                    "You did not enter a country code or you dont have any location",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.Clear.setOnClickListener {
            setClear()
        }

        binding.locationSearch.setOnClickListener {
            getLastLocation()
        }

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mAuthParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .setProfile()
            .setAccessToken()
            .createParams()
        mAuthManager = AccountAuthManager.getService(requireContext(), mAuthParam)
        viewModel.isSignOut.observe(viewLifecycleOwner, Observer {
            if (it) {
                val action = SearchFragmentDirections.actionSearchFragmentToLoginFragment()
                view.findNavController().navigate(action)
            }
        })
    }

    private fun setClear() {
        binding.longitude.text = " "
        binding.latitude.text = " "
    }

    private fun getLastLocation() {
        val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        val task = mFusedLocationProviderClient!!.lastLocation.addOnSuccessListener(
            OnSuccessListener { location ->
                val latitude = location.latitude
                val longitude = location.longitude
                binding.latitude.text = latitude.toString()
                binding.longitude.text = longitude.toString()

                try {
                    val lastLocation =
                        mFusedLocationProviderClient.lastLocation
                    lastLocation.addOnSuccessListener(OnSuccessListener { location ->
                        if (location == null) {
                            Toast.makeText(
                                requireContext(),
                                "location successfully get but your location is null",
                                Toast.LENGTH_LONG
                            ).show()
                            return@OnSuccessListener
                        }
                    })
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Can't get your last location",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Can't get your last location",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }).addOnFailureListener(OnFailureListener {
            Toast.makeText(requireContext(), "Can't get your last location", Toast.LENGTH_LONG)
                .show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                signOut()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        val signOutTask = mAuthManager?.signOut()
        signOutTask?.addOnSuccessListener {
            AGConnectAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "signout success", Toast.LENGTH_LONG).show()
            viewModel.userSignedOut(true)
        }?.addOnFailureListener {
            Toast.makeText(requireContext(), "signout fail", Toast.LENGTH_LONG).show()
            viewModel.userSignedOut(false)
        }
    }

}
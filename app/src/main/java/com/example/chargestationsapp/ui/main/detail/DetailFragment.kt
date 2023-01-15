package com.example.chargestationsapp.ui.main.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Pair
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chargestationsapp.data.EquipmentItem
import com.example.chargestationsapp.databinding.FragmentDetailBinding
import com.example.chargestationsapp.ui.main.adapter.DetailAdapter
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.mlsdk.tts.*

class DetailFragment : Fragment() {


    private lateinit var binding: FragmentDetailBinding

    private lateinit var newRecyclerView: RecyclerView
    lateinit var location: ArrayList<Double>

    private val args by navArgs<DetailFragmentArgs>()

    private lateinit var ttsConfig: MLTtsConfig
    private lateinit var ttsEngine: MLTtsEngine
    private lateinit var ttsCalback: MLTtsCallback

    var lng: LatLng? = null
    var address: String? = null
    var mail: String? = null
    var phonenumber: String? = null
    var site: String? = null
    var equipmentList = ArrayList<EquipmentItem>()

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        arguments?.let {

            lng = args.detail?.location
            address = args.detail?.nearestAddress
            mail = args.detail?.email
            site = args.detail?.website
            phonenumber = args.detail?.phone
            equipmentList = args.detail?.equipmentList as ArrayList<EquipmentItem>
        }
        newRecyclerView = binding.equipmentDetail
        newRecyclerView.layoutManager = LinearLayoutManager(this.context)
        newRecyclerView.setHasFixedSize(true)

        binding.detailLocation.text = lng.toString()
        binding.phone.text = phonenumber
        binding.website.text = site
        binding.mailAddress.text = mail
        binding.addressInfo.text = address
        getUserData()

        MLApplication.getInstance().apiKey =
            "DAEDAOlL6KvWiPM3MUM/dzfTIj/gVk4EUeZmASMEpqf6GrkteBaUjueH8YrUaYurdq4hFNY7j1JqFgoWNtZKxQgH/Aw3U5yazGCCUg=="

        configureEnginePreferences()
        initializeEngine()
        initializeCallback()

        binding.playButton.setOnClickListener {
            if (binding?.addressInfo?.text?.toString()!!.isEmpty()) {
                Toast.makeText(requireContext(), "Empty address", Toast.LENGTH_LONG).show()
            }

            speak(address.toString())

        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

    }

    private fun configureEnginePreferences() {
        ttsConfig = MLTtsConfig().setLanguage("en-US")
            .setPerson("en-US" + "-st-2")
            .setSpeed(1f)
            .setVolume(1f)
    }

    private fun initializeEngine() {
        ttsEngine = MLTtsEngine(ttsConfig)
    }

    private fun initializeCallback() {
        ttsCalback = object : MLTtsCallback {

            override fun onError(p0: String?, p1: MLTtsError?) {
            }

            override fun onWarn(p0: String?, p1: MLTtsWarn?) {
            }

            override fun onRangeStart(p0: String?, p1: Int, p2: Int) {

            }

            override fun onAudioAvailable(
                p0: String?,
                p1: MLTtsAudioFragment?,
                p2: Int,
                p3: Pair<Int, Int>?,
                p4: Bundle?
            ) {

            }

            override fun onEvent(taskId: String?, eventId: Int, bundle: Bundle?) {
                when (eventId) {
                    MLTtsConstants.EVENT_PLAY_START -> {
                    }
                    MLTtsConstants.EVENT_PLAY_PAUSE -> {
                    }
                    MLTtsConstants.EVENT_PLAY_RESUME -> {
                    }
                    MLTtsConstants.EVENT_PLAY_STOP -> {
                    }
                    MLTtsConstants.EVENT_SYNTHESIS_START -> {
                    }
                    MLTtsConstants.EVENT_SYNTHESIS_END -> {
                    }
                    MLTtsConstants.EVENT_SYNTHESIS_COMPLETE -> {
                    }
                }
            }

        }
        ttsEngine.setTtsCallback(ttsCalback)
    }


    fun getUserData() {
        newRecyclerView.adapter = DetailAdapter(equipmentList)
    }

    private fun speak(text: String) = ttsEngine.speak(text, MLTtsEngine.QUEUE_APPEND)

}
package com.appcake.modsforvalheim.core.ui.fragments.cheat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.adapter.cheat.DropDownCheatAdapter
import com.appcake.modsforvalheim.core.adapter.command.DropDownCommandAdapter
import com.appcake.modsforvalheim.core.model.ArmorData
import com.appcake.modsforvalheim.core.model.DropDownData
import com.appcake.modsforvalheim.core.ui.MainActivity
import com.appcake.modsforvalheim.databinding.FragmentCheatBinding
import com.example.dropbox_base.models.CheatsResponse
import com.example.dropbox_base.models.CommandsResponse
import com.example.dropbox_base.utils.Constantdropbox
import com.example.dropbox_base.utils.DropboxServices
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.io.File

class CheatFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentCheatBinding
    private lateinit var navController: NavController
    private var cheatsDataList: ArrayList<DropDownData>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        (activity as MainActivity).showLoader()
        binding.backBtn.setOnClickListener(this)
        setUpObserverForDropbox()
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun setUpObserverForDropbox() {
        DropboxServices.getInstance(requireActivity()).loadFileDataDrop(
            jsonFilePathDropbox = Constantdropbox.CHEATS_JSON_PATH_DROPBOX,
            cacheDirPath = File(requireActivity().filesDir, "source${File.separator}cheats")
        )
        DropboxServices.getInstance(requireContext())
            .getJsonResponse(object : DropboxServices.OnJsonResult {
                override fun getJsonResult(jsonFilePath: String) {
                    if (jsonFilePath != "") {
                        val namesJsonRes = DropboxServices.getInstance(requireContext()).getJsonRes(
                            jsonFilePath, CheatsResponse::class.java
                        )
                        cheatsDataList = ArrayList<DropDownData>()
                        for (json in namesJsonRes.val2g8f_list!!) {
                            var cheats = json?.name?.let {
                                json?.des?.let { it1 ->
                                    DropDownData(
                                        it,
                                        it1
                                    )
                                }
                            }
                            if (cheats != null) {
                                cheatsDataList!!.add(cheats)
                            }
                        }

                        binding.elvCommand.setAdapter(
                            DropDownCheatAdapter(
                                requireContext(), cheatsDataList!!
                            )
                        )
                        (activity as MainActivity).hideLoader()
                    }

                }

                override fun getJsonError() {
                }
            })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn -> {
                navController.popBackStack()
            }
        }
    }

}
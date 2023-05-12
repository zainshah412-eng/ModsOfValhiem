package com.appcake.modsforvalheim.core.ui.fragments.commands

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.appcake.modsforvalheim.core.adapter.command.DropDownCommandAdapter
import com.appcake.modsforvalheim.core.model.ArmorData
import com.appcake.modsforvalheim.core.model.DropDownData
import com.appcake.modsforvalheim.core.ui.MainActivity
import com.appcake.modsforvalheim.databinding.FragmentCommandsBinding
import com.example.dropbox_base.models.CommandsResponse
import com.example.dropbox_base.utils.Constantdropbox.COMMANDS_JSON_PATH_DROPBOX
import com.example.dropbox_base.utils.DropboxServices
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.io.File

class CommandsFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentCommandsBinding
    private lateinit var navController: NavController
    private var commandsDataList: ArrayList<DropDownData>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommandsBinding.inflate(inflater,container,false)
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

    private fun setUpObserverForDropbox(){
        DropboxServices.getInstance(requireActivity()).loadFileDataDrop(
            jsonFilePathDropbox = COMMANDS_JSON_PATH_DROPBOX,
            cacheDirPath = File(requireActivity().filesDir, "source${File.separator}commands")
        )
        DropboxServices.getInstance(requireContext())
            .getJsonResponse(object : DropboxServices.OnJsonResult{
                override fun getJsonResult(jsonFilePath: String) {
                    if(jsonFilePath!="") {
                        val namesJsonRes = DropboxServices.getInstance(requireContext()).getJsonRes(
                            jsonFilePath, CommandsResponse::class.java
                        )
                        commandsDataList = ArrayList<DropDownData>()
                        for(json in namesJsonRes.jsonCommandsglxList!!){
                            val commands = json?.glx_d4?.let {
                                json.glx_i1?.let { it1 ->
                                    DropDownData(
                                        it,
                                        it1
                                    )
                                }
                            }
                            if (commands != null) {
                                commandsDataList!!.add(commands)
                            }
                        }

                        binding.elvCommand.setAdapter(
                            DropDownCommandAdapter(
                                requireContext(), commandsDataList!!
                            )
                        )
                        (activity as MainActivity).hideLoader()
                        //  Log.wtf("db",namesJsonRes.toString())

                    }

                }

                override fun getJsonError() {
                }
            })

        //     mapsAdapter.notifyDataSetChanged()
    }


    override fun onClick(v: View?) {
        when(v){
            binding.backBtn ->{
                navController.popBackStack()
            }

        }
    }

}
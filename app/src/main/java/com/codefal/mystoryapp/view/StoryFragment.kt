package com.codefal.mystoryapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codefal.mystoryapp.R
import com.codefal.mystoryapp.databinding.FragmentStoryListBinding
import com.codefal.mystoryapp.model.ListStoryItem
import com.codefal.mystoryapp.view.adapter.MyItemAdapter
import com.codefal.mystoryapp.viewmodel.PrefViewModel
import com.codefal.mystoryapp.viewmodel.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryFragment : Fragment() {

    private var _binding : FragmentStoryListBinding? = null
    private val binding get() = _binding!!

    private val prefModel : PrefViewModel by viewModels()
    private val storyModel : StoryViewModel by viewModels()
    private val adapterStory by lazy { MyItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryListBinding.inflate(layoutInflater)
        storyModel.loadingObserver().observe(viewLifecycleOwner){ loading(it) }
        storyModel.messageObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setRV()

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.home)

        binding.btnCreateStory.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_storyFragment_to_createStoryFragment)
        }

        binding.btnActionMaps.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

    }

    private fun setData() {
        prefModel.getToken().observe(viewLifecycleOwner){ token ->
            Log.i("Pref", "Token: $token")
            storyModel.getStory(token)
            storyModel.listStoryObserver().observe(viewLifecycleOwner){
                if (it != null){
                    adapterStory.setData(it as MutableList<ListStoryItem?>)
                    setRV()
                    Log.i("Success", "onViewCreated: List Story")
                }
            }
        }
    }

    private fun setRV() {
        binding.storyList.adapter = adapterStory
        val layout = LinearLayoutManager(context)
        binding.storyList.layoutManager = layout
        adapterStory.onClick ={
            val bundle = Bundle()
            bundle.putParcelable(KEY_STORY, it)
            findNavController().navigate(R.id.action_storyFragment_to_detailFragment, bundle)
        }
    }

    private fun loading(status: Boolean) {
        binding.loadingBar.isVisible = status
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val KEY_STORY = "detail_story"
    }
}
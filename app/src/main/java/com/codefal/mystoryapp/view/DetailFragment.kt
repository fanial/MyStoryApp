package com.codefal.mystoryapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.codefal.mystoryapp.R
import com.codefal.mystoryapp.databinding.FragmentDetailBinding
import com.codefal.mystoryapp.network.model.ListStoryItem
import com.codefal.mystoryapp.view.StoryFragment.Companion.KEY_STORY
import com.codefal.mystoryapp.viewmodel.PrefViewModel
import com.codefal.mystoryapp.viewmodel.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val prefModel : PrefViewModel by viewModels()
    private val storyModel : StoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        storyModel.loadingObserver().observe(viewLifecycleOwner){ loading(it) }
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.detail_story)

        val getArgs = arguments?.getParcelable<ListStoryItem>(KEY_STORY)
        val idStory = getArgs?.id
        prefModel.getToken().observe(viewLifecycleOwner){ token ->
            Log.i("Pref", "Token: $token")
            storyModel.getDetail(token, idStory!!)
            storyModel.detailStoryObserver().observe(viewLifecycleOwner){ story ->
                if (story != null){
                    binding.tvDetailName.text = story.name
                    binding.tvDetailDescription.text = story.description
                    Glide.with(this).load(story.photoUrl).into(binding.ivDetailPhoto)
                }else{
                    storyModel.messageObserver().observe(viewLifecycleOwner){ msg ->
                        if (msg !=null){
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

    }

    private fun loading(status: Boolean) {
        binding.loadingBar.isVisible = status
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
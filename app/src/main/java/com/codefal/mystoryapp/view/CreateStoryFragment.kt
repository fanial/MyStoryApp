package com.codefal.mystoryapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.codefal.mystoryapp.R
import com.codefal.mystoryapp.databinding.FragmentCreateStoryBinding
import com.codefal.mystoryapp.utils.reduceFileImage
import com.codefal.mystoryapp.utils.uriToFile
import com.codefal.mystoryapp.viewmodel.PrefViewModel
import com.codefal.mystoryapp.viewmodel.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CreateStoryFragment : Fragment() {

    private var _binding : FragmentCreateStoryBinding? = null
    private val binding get() = _binding!!

    private val prefModel : PrefViewModel by viewModels()
    private val storyModel : StoryViewModel by viewModels()
    private var getFile: File? = null

    private var permissionRequestCount: Int = 0
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        binding.imagePreview.setImageURI(uri)
        val imgUri = uri as Uri
        val img = uriToFile(imgUri, requireContext())
        getFile = img
        Log.i("URI Gallery", "Image: $uri, $img")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateStoryBinding.inflate(layoutInflater)
        storyModel.loadingObserver().observe(viewLifecycleOwner){ loading(it) }
        storyModel.messageObserver().observe(viewLifecycleOwner){
            if (it !=null){
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionsIfNecessary()

        savedInstanceState?.let {
            permissionRequestCount = it.getInt(KEY_PERMISSIONS_REQUEST_COUNT, 0)
        }

        binding.galleryButton.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.buttonAdd.setOnClickListener {
            uploadStory()
        }

    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = getFile as File
            prefModel.getToken().observe(viewLifecycleOwner){
                val token = it
                val desc = binding.edAddDescription.text.toString()
                val image = reduceFileImage(file)
                val currentImageFile : RequestBody = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo", file.name , currentImageFile
                )
                Log.i("Image", "uploadStory: $image")
                storyModel.addStory(token, desc.toRequestBody("text/plain".toMediaTypeOrNull()), imageMultipart)
                Navigation.findNavController(requireView()).navigate(R.id.action_createStoryFragment_to_storyFragment)
            }
        } else {
            Toast.makeText(context, getString(R.string.input_image), Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {

            if (permissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                permissionRequestCount += 1
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.Allow_perm),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in permissions) {
            hasPermissions = hasPermissions && isPermissionGranted(permission)
        }
        return hasPermissions
    }

    private fun isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(requireContext(), permission) ==
            PackageManager.PERMISSION_GRANTED

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> requestPermissionsIfNecessary()
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PERMISSIONS_REQUEST_COUNT, permissionRequestCount)
    }

    private fun loading(status: Boolean) {
        when(status){
            true -> {
                binding.loadingBar.visibility = View.VISIBLE
            }
            false -> {
                binding.loadingBar.visibility = View.GONE
            }
        }
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 101
        const val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
        const val MAX_NUMBER_REQUEST_PERMISSIONS = 2
    }
}
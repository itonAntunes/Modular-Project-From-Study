package com.itonAntunes.mainactivity.core.toast.workmanager


import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.itonAntunes.mainactivity.R
import com.itonAntunes.mainactivity.databinding.FragmentSelectImageBinding

class SelectImageFragment : Fragment(R.layout.fragment_select_image) {

    companion object {
        private const val KEY_PERMISSIONS_GRANTED = "KEY_PERMISSIONS_GRANTED"
        private const val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
        private const val MAX_NUMBER_REQUEST_PERMISSIONS = 2
    }
    private var permissionsRequestCount: Int = 0
    private lateinit var binding: FragmentSelectImageBinding
    private lateinit var launcher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            handleImageRequestResult(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSelectImageBinding.bind(view)

        //recuperando o ultimo estado caso o usuario rotacione o aparelho
        savedInstanceState?.let{
            permissionsRequestCount = it.getInt(KEY_PERMISSIONS_REQUEST_COUNT,0)
            userHasPermission = it.getBoolean(KEY_PERMISSIONS_GRANTED, false)
        }
        //assegurar que o usuario tem as permissões necessárias
        requestPermissionsOnlyTwice(userHasPermission)
        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }
    }

    private var userHasPermission = false
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private fun requestPermissionsOnlyTwice(hasPermissionsAlready: Boolean){
        if(!hasPermissionsAlready){
            if(permissionsRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS){
                permissionsRequestCount += 1
                // nova api : Atenção
                val permissionChecker = registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { acceptedPermissions ->
                    val permissionsIdentified = acceptedPermissions.all{it.key in permissions}
                    val permissionsGrant = acceptedPermissions.all{it.value == true}
                    if (permissionsIdentified && permissionsGrant){
                        permissionsRequestCount = 0
                        userHasPermission = true
                    }
                }
                if(!userHasPermission) {
                    permissionChecker.launch(permissions)
                }
            } else {
                Toast.makeText(requireContext(),
                    R.string.set_permissions_in_settings,
                    Toast.LENGTH_LONG
                ).show()
                binding.selectImage.isEnabled = false
            }
        }
    }
    private fun handleImageRequestResult(uri: Uri){

        //Navegar para a proxima etapa onde exibo as opções de blur
        navTo(R.id.blurFragment, bundleOf(Pair(KEY_IMAGE_URI, uri.toString())))
        //findNavController().navigate(R.id.blurFragment, bundleOf(Pair(KEY_IMAGE_URI, uri.ToString())))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PERMISSIONS_REQUEST_COUNT, permissionsRequestCount)
        outState.putBoolean(KEY_PERMISSIONS_GRANTED, userHasPermission)
    }
}
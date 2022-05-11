package com.wiryadev.binar_movie.ui.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.wiryadev.binar_movie.R
import com.wiryadev.binar_movie.data.local.entity.UserEntity
import com.wiryadev.binar_movie.databinding.FragmentProfileBinding
import com.wiryadev.binar_movie.ui.*
import com.wiryadev.binar_movie.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private var email = ""

    private lateinit var currentPhotoPath: String

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            viewModel.assignFile(myFile)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireActivity())
            viewModel.assignFile(myFile)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivProfile.setOnClickListener {
            chooseImageDialog()
        }

        viewModel.userSession.observe(viewLifecycleOwner) { user ->
            if (user.email.isEmpty()) {
                val intent = Intent(context, AuthActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

            if (email != user.email) {
                email = user.email
                viewModel.getUser(email)
            }
        }

        with(binding) {
            viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
                uiState.file?.let {
                    ivProfile.load(it) {
                        transformations(CircleCropTransformation())
                    }
                }

                if (uiState.result > 0) {
                    root.showSnackbar(getString(R.string.update_success))
                }

                uiState.user?.let { user ->
                    etUsername.setText(user.username)
                    etFullName.setText(user.fullName ?: "")
                    etBirthDate.setText(user.birthDate?.formatDisplayDate() ?: "")
                    etAddress.setText(user.address ?: "")
                    handleUpdate(user = user)
                }
            }

            btnLogOut.setOnClickListener {
                viewModel.logout()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    context?.applicationContext,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun handleUpdate(user: UserEntity) {
        var dateForDatabase = user.birthDate ?: ""

        with(binding) {
            etBirthDate.isEnabled = true
            etBirthDate.setOnClickListener {
                val datePicker = buildDatePicker()
                datePicker.addOnPositiveButtonClickListener { selection ->
                    val date = Date(selection)
                    dateForDatabase = simpleDateFormat.format(date)
                    etBirthDate.setText(dateForDatabase.formatDisplayDate())
                }
                datePicker.show(childFragmentManager, "date")
            }

            btnUpdate.setOnClickListener {
                val newFullName = etFullName.text.toString().trim()
                val newBirthDate = dateForDatabase
                val newAddress = etAddress.text.toString().trim()

                user.apply {
                    fullName = newFullName
                    birthDate = newBirthDate
                    address = newAddress
                }

                viewModel.updateUser(user = user)
            }
        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -15) //Goes 15 Year Back in time
        val upperLimit: Long = calendar.timeInMillis

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.before(upperLimit))

        return MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(upperLimit)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> startGallery() }
            .setNegativeButton("Camera") { _, _ -> startTakePhoto() }
            .show()
    }


    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext().applicationContext,
                "com.wiryadev.binar_movie",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

}

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
private const val REQUEST_CODE_PERMISSIONS = 10
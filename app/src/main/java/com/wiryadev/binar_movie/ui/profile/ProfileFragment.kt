package com.wiryadev.binar_movie.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
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
import com.wiryadev.binar_movie.ui.auth.AuthActivity
import com.wiryadev.binar_movie.ui.formatDisplayDate
import com.wiryadev.binar_movie.ui.showSnackbar
import com.wiryadev.binar_movie.ui.simpleDateFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private var email = ""

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.ivProfile.load(result) {
                transformations(CircleCropTransformation())
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
            //intent to open camera app
            checkingPermissions()
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

    private fun checkingPermissions() {
        if (
            isGranted(
                requireActivity(),
                Manifest.permission.CAMERA,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        binding.ivProfile.load(bitmap) {
            transformations(CircleCropTransformation())
        }
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

}

private val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
private const val REQUEST_CODE_PERMISSION = 100
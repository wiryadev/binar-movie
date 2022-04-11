package com.wiryadev.binar_movie.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
package com.theateam.nexoncord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.theateam.nexoncord.databinding.DialogChangePasswordBinding

class ChangePasswordDialogFragment : DialogFragment() {
    private var _binding: DialogChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnChangePassword.setOnClickListener {
            val currentPassword = binding.etCurrentPassword.text.toString()
            val newPassword = binding.etNewPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (validateInputs(currentPassword, newPassword, confirmPassword)) {
                changePassword(currentPassword, newPassword)
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun validateInputs(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        if (currentPassword.isEmpty()) {
            binding.etCurrentPassword.error = "Current password is required"
            binding.etCurrentPassword.requestFocus()
            return false
        }

        if (newPassword.isEmpty()) {
            binding.etNewPassword.error = "New password is required"
            binding.etNewPassword.requestFocus()
            return false
        }

        if (newPassword.length < 6) {
            binding.etNewPassword.error = "Min password length should be 6 characters"
            binding.etNewPassword.requestFocus()
            return false
        }

        if (confirmPassword != newPassword) {
            binding.etConfirmPassword.error = "Passwords don't match"
            binding.etConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun changePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser
        val email = user?.email

        if (email != null) {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)

            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Password changed successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dismiss()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed to change password: ${updateTask.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed: ${reauthTask.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.theateam.nexoncord

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.theateam.nexoncord.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        email = intent.getStringExtra("email") ?: ""

        binding.btnVerifyCode.setOnClickListener {
            val code = binding.etVerificationCode.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInputs(code, newPassword, confirmPassword)) {
                // In a real app, you would verify the code from your backend
                // For now, we'll assume the code is correct and reset the password
                resetPassword(newPassword)
            }
        }
    }

    private fun validateInputs(code: String, newPassword: String, confirmPassword: String): Boolean {
        if (code.isEmpty()) {
            binding.etVerificationCode.error = "Verification code is required"
            binding.etVerificationCode.requestFocus()
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

    private fun resetPassword(newPassword: String) {
        binding.progressBar.visibility = android.view.View.VISIBLE

        val user = auth.currentUser
        if (user != null && user.email == email) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Password reset successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Failed to reset password: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    binding.progressBar.visibility = android.view.View.GONE
                }
        } else {
            // If the user is not logged in, we need to sign in with the old credentials
            // This is a simplified version - in a real app, you'd have a more secure method
            Toast.makeText(
                this,
                "Please try again later",
                Toast.LENGTH_SHORT
            ).show()
            binding.progressBar.visibility = android.view.View.GONE
        }
    }
}

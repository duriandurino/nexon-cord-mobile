package com.theateam.nexoncord

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.theateam.nexoncord.ConfirmDialogFragment
import com.theateam.nexoncord.MainActivity
import com.theateam.nexoncord.R
import com.theateam.nexoncord.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        binding.btnAboutUs.setOnClickListener {
            showAboutUsDialog()
        }

        binding.btnLogout.setOnClickListener {
            (requireActivity() as MainActivity).logout()
        }
    }

    private fun showChangePasswordDialog() {
        val dialog = ChangePasswordDialogFragment()
        dialog.show(parentFragmentManager, "ChangePasswordDialog")
    }

    private fun showAboutUsDialog() {
        val dialog = AboutUsDialogFragment()
        dialog.show(parentFragmentManager, "AboutUsDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.theateam.nexoncord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.theateam.nexoncord.databinding.DialogAboutUsBinding


class AboutUsDialogFragment : DialogFragment() {
    private var _binding: DialogAboutUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set your about us content here
        binding.tvAppName.text = "Nexon Cord"
        binding.tvVersion.text = "Version 1.0.0"
        binding.tvDescription.text = "IoT-powered smart extension cord with remote control capabilities"
        binding.tvCompany.text = "© 2024 Your Company Name"

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
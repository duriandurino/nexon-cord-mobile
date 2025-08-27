package com.theateam.nexoncord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.theateam.nexoncord.databinding.FragmentControlsBinding

class ControlsFragment : Fragment() {
    private var _binding: FragmentControlsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var deviceId: String? = null
    private var deviceRef: DatabaseReference? = null
    private var valueEventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Get device ID from arguments
        deviceId = arguments?.getString("deviceId")

        if (deviceId.isNullOrEmpty()) {
            // Handle error - no device ID provided
            requireActivity().onBackPressed()
            return
        }

        setupUI()
        setupFirebaseListeners()
    }

    private fun setupUI() {
        binding.switchOutlet1.setOnCheckedChangeListener { _, isChecked ->
            updateOutletState(1, isChecked)
        }

        binding.switchOutlet2.setOnCheckedChangeListener { _, isChecked ->
            updateOutletState(2, isChecked)
        }

        binding.switchOutlet3.setOnCheckedChangeListener { _, isChecked ->
            updateOutletState(3, isChecked)
        }

        binding.switchOutlet4.setOnCheckedChangeListener { _, isChecked ->
            updateOutletState(4, isChecked)
        }

        binding.btnAllOn.setOnClickListener {
            setAllOutlets(true)
        }

        binding.btnAllOff.setOnClickListener {
            setAllOutlets(false)
        }
    }

    private fun setupFirebaseListeners() {
        val userId = auth.currentUser?.uid ?: return
        deviceRef = database.getReference("devices/$deviceId")

        valueEventListener = deviceRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    // Device doesn't exist or user doesn't have access
                    return
                }

                // Update UI with current outlet states
                val outlet1 = snapshot.child("outlet1").getValue(Boolean::class.java) ?: false
                val outlet2 = snapshot.child("outlet2").getValue(Boolean::class.java) ?: false
                val outlet3 = snapshot.child("outlet3").getValue(Boolean::class.java) ?: false
                val outlet4 = snapshot.child("outlet4").getValue(Boolean::class.java) ?: false

                // Remove listeners to avoid infinite loops
                binding.switchOutlet1.setOnCheckedChangeListener(null)
                binding.switchOutlet2.setOnCheckedChangeListener(null)
                binding.switchOutlet3.setOnCheckedChangeListener(null)
                binding.switchOutlet4.setOnCheckedChangeListener(null)

                // Update switch states
                binding.switchOutlet1.isChecked = outlet1
                binding.switchOutlet2.isChecked = outlet2
                binding.switchOutlet3.isChecked = outlet3
                binding.switchOutlet4.isChecked = outlet4

                // Restore listeners
                setupUI()

                // Update device status
                val status = snapshot.child("status").getValue(String::class.java) ?: "offline"
                binding.tvDeviceStatus.text = "Status: $status"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateOutletState(outlet: Int, state: Boolean) {
        deviceRef?.child("outlet$outlet")?.setValue(state)
    }

    private fun setAllOutlets(state: Boolean) {
        val updates = mapOf(
            "outlet1" to state,
            "outlet2" to state,
            "outlet3" to state,
            "outlet4" to state
        )

        deviceRef?.updateChildren(updates)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        valueEventListener?.let { deviceRef?.removeEventListener(it) }
        _binding = null
    }
}
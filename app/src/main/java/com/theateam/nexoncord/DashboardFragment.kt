package com.theateam.nexoncord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theateam.nexoncord.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: DeviceAdapter
    private val deviceList = mutableListOf<Device>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Setup RecyclerView
        // In DashboardFragment.kt, update the DeviceAdapter usage
        adapter = DeviceAdapter(deviceList) { device ->
            val bundle = Bundle().apply {
                putString("deviceId", device.id)
            }

            findNavController().navigate(R.id.navigation_controls, bundle)
        }

        binding.rvDevices.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDevices.adapter = adapter

        // Load user's devices
        loadDevices()

        binding.fabAddDevice.setOnClickListener {
            showAddDeviceDialog()
        }
    }

    private fun loadDevices() {
        val userId = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance()
        val devicesRef = database.getReference("users/$userId/devices")

        devicesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                deviceList.clear()
                for (deviceSnapshot in snapshot.children) {
                    val device = deviceSnapshot.getValue(Device::class.java)
                    device?.id = deviceSnapshot.key
                    device?.let { deviceList.add(it) }
                }
                adapter.notifyDataSetChanged()

                if (deviceList.isEmpty()) {
                    binding.tvNoDevices.visibility = View.VISIBLE
                } else {
                    binding.tvNoDevices.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun showAddDeviceDialog() {
        // Implement dialog to add new device
        // User should input device ID (from ESP32) and a friendly name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class Device(
    var id: String? = null,
    val name: String = "",
    val status: String = "offline"
)
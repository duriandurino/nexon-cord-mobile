package com.theateam.nexoncord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.theateam.nexoncord.R

class DeviceAdapter(
    private val devices: MutableList<Device>,
    private val onDeviceClick: (Device) -> Unit
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.tvDeviceName)
        val deviceStatus: TextView = itemView.findViewById(R.id.tvDeviceStatus)
        val deviceIcon: ImageView = itemView.findViewById(R.id.ivDeviceIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]

        holder.deviceName.text = device.name
        holder.deviceStatus.text = device.status

        // Set different icon based on status
        val statusIcon = if (device.status == "online") {
            R.drawable.ic_device_online
        } else {
            R.drawable.ic_device_offline
        }
        holder.deviceIcon.setImageResource(statusIcon)

        holder.itemView.setOnClickListener {
            onDeviceClick(device)
        }
    }

    override fun getItemCount(): Int = devices.size

    fun updateDevices(newDevices: List<Device>) {
        devices.clear()
        devices.addAll(newDevices)
        notifyDataSetChanged()
    }
}
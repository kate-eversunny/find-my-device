package com.androidgang.findmydevice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.androidgang.findmydevice.R
import com.androidgang.findmydevice.helpers.Constants.sdf
import com.androidgang.findmydevice.models.Smartphone
import java.util.*

// Адаптер для фильмов
class DevicesAdapter : BaseAdapter<Smartphone>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Smartphone> {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        )
    }

    class ViewHolder(itemView: View) : BaseViewHolder<Smartphone>(itemView = itemView) {
        override fun bind(model: Smartphone) {
            itemView.findViewById<TextView>(R.id.tv_item_device_name).text = model.name
            itemView.findViewById<TextView>(R.id.tv_item_device_imei).text = "IMEI: ${model.imei}"
            itemView.findViewById<TextView>(R.id.tv_item_device_last_seen).text = "Last seen: ${sdf.format(Date(model.lastSeen))}"
        }
    }
}
package com.example.chargestationsapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chargestationsapp.R
import com.example.chargestationsapp.data.EquipmentItem
import com.huawei.hms.maps.model.LatLng

class DetailAdapter(private val equipmentList: ArrayList<EquipmentItem>): RecyclerView.Adapter<DetailAdapter.DetailViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.equipment_item,parent,false)
        return DetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
                val currentItem =equipmentList[position]
                holder.title?.text = currentItem?.title
                holder.amper?.text = currentItem?.amps.toString() + "V"
                holder.powerKw?.text = currentItem?.powerKw.toString() + "A"
                holder.voltage?.text = currentItem?.voltage.toString() + "V"
        }

    override fun getItemCount(): Int {
        return equipmentList.size
    }

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.stationName)
        val amper: TextView = itemView.findViewById(R.id.amper)
        val voltage: TextView = itemView.findViewById(R.id.volt)
        val powerKw: TextView = itemView.findViewById(R.id.powerKw)
    }




}
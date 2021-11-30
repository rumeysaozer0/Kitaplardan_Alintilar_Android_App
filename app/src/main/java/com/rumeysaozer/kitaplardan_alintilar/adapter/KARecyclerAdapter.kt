package com.rumeysaozer.kitaplardan_alintilar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rumeysaozer.kitaplardan_alintilar.R
import com.rumeysaozer.kitaplardan_alintilar.model.Alinti
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*

class KARecyclerAdapter (val alintiList : ArrayList<Alinti>) : RecyclerView.Adapter<KARecyclerAdapter.AlintiHolder>() {

    class AlintiHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlintiHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row, parent, false)
        return AlintiHolder(view)
    }

    override fun getItemCount(): Int {
        return alintiList.size
    }
    override fun onBindViewHolder(holder: AlintiHolder, position: Int) {
        holder.itemView.r_kitapAdi.text = alintiList[position].kitapAdi
        holder.itemView.r_yazarAd.text = alintiList[position].yazarAd
        holder.itemView.r_alinti.text =alintiList[position].alinti
        Picasso.get().load(alintiList[position].imageurl).into(holder.itemView.r_image)
    }


}

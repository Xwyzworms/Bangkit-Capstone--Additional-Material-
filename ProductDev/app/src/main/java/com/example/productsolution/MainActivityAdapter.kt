/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.productsolution.databinding.RvMainLayoutBinding

class MainActivityAdapter(private val listContents : ArrayList<String>,
                          private val listener : (String) -> Unit) : RecyclerView.Adapter<MainActivityAdapter.MainViewHolder>() {
    inner class MainViewHolder(private val view : RvMainLayoutBinding ) : RecyclerView.ViewHolder(view.root)
    {
        fun bind(content : String)
        {
            view.tvMainIdTitle.text = content
            itemView.setOnClickListener {
                listener(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val view : RvMainLayoutBinding = RvMainLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listContents.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(listContents[position])
    }
}
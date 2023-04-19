/*
#       Written by : Rose (Pratama Azmi A)
#       Date : Unknown 
#       Text editor : AndroidStudio + VIM
*/
package com.example.productsolution.imageStudyCases

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.productsolution.databinding.RvCameraxMainLayoutBinding

class ImageMLMainAdapter(
    private val context : Context,
    private val listContents : ArrayList<Int>,
    private val listener : (Int) -> Unit )  : RecyclerView.Adapter<ImageMLMainAdapter.MainImageMLViewHolder>()
{
    inner class MainImageMLViewHolder(private val view : RvCameraxMainLayoutBinding) : RecyclerView.ViewHolder(view.root)
    {
        fun bind(content : Int  )
        {
            view.ivImageContent.setImageDrawable(AppCompatResources.getDrawable(context, content))
            itemView.setOnClickListener {
                listener(content)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainImageMLViewHolder {
        val view : RvCameraxMainLayoutBinding = RvCameraxMainLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return MainImageMLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listContents.size
    }

    override fun onBindViewHolder(holder: MainImageMLViewHolder, position: Int) {
        holder.bind(listContents[position])
    }
}
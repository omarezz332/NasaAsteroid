package com.udacity.asteroidradar.main

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.databinding.AstroidItemBinding

class MainAdapter(private val onClickListener: OnClickListener) : ListAdapter<Asteroid, MainAdapter.MainViewHolder>(DiffCallback) {

    class MainViewHolder(private val viewDataBinding: AstroidItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(asteroid: Asteroid) {
            viewDataBinding.asteroid = asteroid
            viewDataBinding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(AstroidItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }
    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

}
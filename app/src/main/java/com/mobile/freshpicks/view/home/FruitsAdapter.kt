package com.mobile.freshpicks.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.freshpicks.data.api.response.FruitList
import com.mobile.freshpicks.databinding.FruitsListBinding

class FruitsAdapter(private val viewModel: HomeViewModel, private val homeFragment: HomeFragment) : ListAdapter<FruitList, FruitsAdapter.FruitsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitsViewHolder {
        val binding = FruitsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FruitsViewHolder(binding, homeFragment)
    }

    override fun onBindViewHolder(holder: FruitsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class FruitsViewHolder(private val binding: FruitsListBinding, private val fragment: HomeFragment) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FruitList) {
            Glide.with(fragment)
                .load(item.fruitImageURL)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = item.name
            binding.tvItemDesc.text = item.description
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FruitList>() {
            override fun areItemsTheSame(oldItem: FruitList, newItem: FruitList): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: FruitList, newItem: FruitList): Boolean {
                return oldItem == newItem
            }
        }
    }
}
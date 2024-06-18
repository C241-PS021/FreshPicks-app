package com.mobile.freshpicks.view.savedresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.freshpicks.data.api.response.FruitList
import com.mobile.freshpicks.data.api.response.ScanHistoryItem
import com.mobile.freshpicks.databinding.FruitsListBinding
import com.mobile.freshpicks.databinding.SavedListBinding
import com.mobile.freshpicks.view.home.FruitsAdapter
import com.mobile.freshpicks.view.home.HomeFragment
import com.mobile.freshpicks.view.home.HomeViewModel

class SavedAdapter(
    private val viewModel: SavedViewModel,
    private val savedFragment: SavedResultFragment
) : ListAdapter<ScanHistoryItem, SavedAdapter.SavedViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedViewHolder {
        val binding = SavedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedViewHolder(viewModel, binding, savedFragment)
    }

    override fun onBindViewHolder(holder: SavedViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SavedViewHolder(
        private val viewModel: SavedViewModel,
        private val binding: SavedListBinding,
        private val fragment: SavedResultFragment
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScanHistoryItem) {
            Glide.with(fragment)
                .load(item.scannedImageURL)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = item.fruitName
            binding.tvItemScanResult.text = item.scanResult

            binding.fabDelete.setOnClickListener {
                viewModel.deleteResultById(item.id)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScanHistoryItem>() {
            override fun areItemsTheSame(oldItem: ScanHistoryItem, newItem: ScanHistoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ScanHistoryItem, newItem: ScanHistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
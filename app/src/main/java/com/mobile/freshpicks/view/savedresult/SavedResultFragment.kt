package com.mobile.freshpicks.view.savedresult

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.freshpicks.R
import com.mobile.freshpicks.databinding.FragmentSavedResultBinding
import com.mobile.freshpicks.helper.ViewModelFactory
import com.mobile.freshpicks.view.home.FruitsAdapter

class SavedResultFragment : Fragment() {

    private var _binding: FragmentSavedResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var savedAdapter: SavedAdapter

    private val viewModel by viewModels<SavedViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading -> if (isLoading) showLoading(true) else showLoading(false) }

        setupHomeScreen()
    }

    private fun setupHomeScreen() {
        savedAdapter = SavedAdapter(viewModel, this)
        binding.rvResult.adapter = savedAdapter
        binding.rvResult.layoutManager = LinearLayoutManager(context)

        viewModel.getSavedResult()
        viewModel.savedList.observe(viewLifecycleOwner) { results ->
            savedAdapter.submitList(results)
            if (results.isNullOrEmpty()) {
                binding.emptyResultTv.visibility = View.VISIBLE
            } else {
                binding.emptyResultTv.visibility = View.GONE
            }
        }

        binding.deleteAllBtn.setOnClickListener {
            viewModel.deleteAllResult()
        }
    }

    private fun showLoading(isLoading: Boolean) { binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
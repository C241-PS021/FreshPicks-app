package com.mobile.freshpicks.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.freshpicks.R
import com.mobile.freshpicks.databinding.FragmentHomeBinding
import com.mobile.freshpicks.helper.ViewModelFactory


class HomeFragment : Fragment() {

    // Set to nullable to avoid memory leaks
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fruitsAdapter: FruitsAdapter

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.actionLogout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading -> if (isLoading) showLoading(true) else showLoading(false) }

        setupHomeScreen()

    }

    private fun setupHomeScreen() {
        fruitsAdapter = FruitsAdapter(viewModel, this)
        binding.rvFruits.adapter = fruitsAdapter
        binding.rvFruits.layoutManager = LinearLayoutManager(context)

        viewModel.getFruitsList()

        viewModel.fruitList.observe(viewLifecycleOwner) { fruits ->
            fruitsAdapter.submitList(fruits)
        }
    }

    private fun showLoading(isLoading: Boolean) { binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.myproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.data.response.ListEventsItem
import com.example.myproject.databinding.FragmentHomeBinding
import com.example.myproject.ui.DetailActivity
import com.example.myproject.ui.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.rvUpcoming.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcoming.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))

        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            setEventsData(binding.rvUpcoming, events)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            setEventsData(binding.rvFinished, events)
        }


        // Handle loading indicator
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage() // Reset the error message after showing it
            }
        }
        viewModel.fetchUpcomingEvents()
        viewModel.fetchFinishedEvents()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setEventsData(recyclerView: RecyclerView, events: List<ListEventsItem?>?) {
        val adapter = EventAdapter { event ->
            // Start the DetailActivity when an event is clicked
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }
        adapter.submitList(events)
        recyclerView.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
package com.example.myproject.ui.finished

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
import com.example.myproject.databinding.FragmentFinishedBinding
import com.example.myproject.ui.DetailActivity
import com.example.myproject.ui.EventAdapter


class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private lateinit var viewModel: FinishedViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FinishedViewModel::class.java]

        binding.apply {
            searchView.setupWithSearchBar(searchBar)

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString()
                if (query.isNotEmpty()) {
                    // Trigger the search with the entered query
                    viewModel.searchEvents(query)
                    searchView.hide()
                    searchBar.visibility = View.VISIBLE
                } else {
                    searchBar.visibility = View.VISIBLE
                }
                false
            }

            rvFinished.layoutManager = LinearLayoutManager(requireContext())
            rvFinished.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }


        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            setEventsData(binding.rvFinished, events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage() // Reset the error message after showing it
            }
        }
        viewModel.fetchFinishedEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setEventsData(recyclerView: RecyclerView, events: List<ListEventsItem?>?) {
        val adapter = EventAdapter { event ->
            // Handle item click - navigate to DetailActivity with the selected event ID
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id) // Pass event ID to DetailActivity
            startActivity(intent)
        }
        adapter.submitList(events)
        recyclerView.adapter = adapter

        if (events.isNullOrEmpty()) {
            binding.rvFinished.visibility = View.GONE
            binding.tvPlaceholder.visibility = View.VISIBLE
        } else {
            binding.rvFinished.visibility = View.VISIBLE
            binding.tvPlaceholder.visibility = View.GONE
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
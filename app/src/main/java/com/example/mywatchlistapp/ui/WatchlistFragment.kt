package com.example.mywatchlistapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mywatchlistapp.ShowApplication
import com.example.mywatchlistapp.adapters.ShowListAdapter
import com.example.mywatchlistapp.databinding.FragmentWatchlistBinding
import java.text.SimpleDateFormat


class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    // Don't forget to put the 'name' attribute (application) in AndroidManifest.xml
    private val viewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(
            (activity?.application as ShowApplication).database.showDao()
        )
    }

    /** Lifecycle Methods **/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // To remove focus in EditText when fragment starts
        // set android:focusable="true"
        // set android:focusableInTouchMode="true"
        // (in parent layout)

        // Show list
        val adapter = ShowListAdapter { selectedShow ->
            val action =
                WatchlistFragmentDirections.actionWatchlistFragmentToAddFragment(selectedShow.id)
            this.findNavController().navigate(action)
        }

        binding.watchlist.adapter = adapter

        val pattern = "yyyy/MM/dd HH:mm:ss"

        // Search
        binding.search.setOnClickListener {
            val searchFor = binding.title.text.toString()
            viewModel.allShows.observe(viewLifecycleOwner) { shows ->
                shows.let {
                    adapter.submitList(
                        it.filter { show ->
                            show.title.contains(
                                searchFor,
                                true
                            )
                        }
                            .sortedByDescending { show -> SimpleDateFormat(pattern).parse(show.timestamp) }
                    ) {
                        binding.watchlist.scrollToPosition(0)
                    }
                }
            }
        }

        // Clear
        binding.clear.setOnClickListener {
            binding.title.setText("")
            viewModel.allShows.observe(viewLifecycleOwner) { shows ->
                shows.let {
                    adapter.submitList(it.sortedByDescending { show ->
                        SimpleDateFormat(pattern).parse(
                            show.timestamp
                        )
                    }) {
                        binding.watchlist.scrollToPosition(0)
                    }
                }
            }
        }

        viewModel.allShows.observe(viewLifecycleOwner) { shows ->
            shows.let {
                adapter.submitList(
                    it.sortedByDescending { show ->
                        SimpleDateFormat(pattern).parse(
                            show.timestamp
                        )
                    }) {
                    binding.watchlist.scrollToPosition(0)
                }
            }
        }
        binding.watchlist.layoutManager = LinearLayoutManager(requireContext())

        // Add
        binding.add.setOnClickListener {
            val action = WatchlistFragmentDirections.actionWatchlistFragmentToAddFragment(-1)
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.mywatchlistapp.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mywatchlistapp.R
import com.example.mywatchlistapp.ShowApplication
import com.example.mywatchlistapp.data.Show
import com.example.mywatchlistapp.databinding.FragmentAddBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val args: AddFragmentArgs by navArgs()

    // Don't forget to put the 'name' attribute (application) in AndroidManifest.xml
    private val viewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(
            (activity?.application as ShowApplication).database.showDao()
        )
    }

    lateinit var show: Show

    /** Lifecycle Methods **/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.showId != -1) {
            // Retrieve data and display
            viewModel.getSpecificShow(args.showId).observe(viewLifecycleOwner) { selectedShow ->
                show = selectedShow
                bindData(show)
            }
            binding.add.text = "Save"

            // Delete button
            binding.delete.visibility = View.VISIBLE
            binding.delete.setOnClickListener {
                showConfirmationDeleteDialog()
            }
        }

        binding.add.setOnClickListener {
            val title = binding.title.text.toString()
            val rating = binding.ratingBar.rating
            var recommend = ""
            when (binding.recommendRadioGroup.checkedRadioButtonId) {
                binding.yes.id -> recommend = "yes"
                binding.no.id -> recommend = "no"
            }
            val comment = binding.comment.text.toString()
            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
            val timestamp = LocalDateTime.now().format(formatter)
            if (isInputValid(title, rating, recommend)) {
                if (binding.status.visibility == View.VISIBLE) {
                    binding.status.visibility = View.GONE
                }

                if (binding.add.text == "Save") {
                    viewModel.changeShowDetails(
                        show.id,
                        title,
                        rating,
                        recommend,
                        comment,
                        timestamp
                    )
                    Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addNewShow(title, rating, recommend, comment, timestamp)
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT)
                        .show()
                }

                val action = AddFragmentDirections.actionAddFragmentToWatchlistFragment()
                this.findNavController().navigate(action)
            } else {
                binding.status.text = getString(R.string.please_fill)
                binding.status.visibility = View.VISIBLE
            }
        }

        binding.cancel.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Other Methods **/

    private fun isInputValid(
        title: String,
        rating: Float,
        recommend: String
    ): Boolean {
        return viewModel.isInputValid(title, rating, recommend)
    }

    private fun bindData(show: Show) {
        binding.apply {
            title.setText(show.title)
            ratingBar.rating = show.rating

            // The id of a radio button is different from its index
            if (show.recommend == "yes") {
                recommendRadioGroup.check(recommendRadioGroup[0].id)
            } else {
                recommendRadioGroup.check(recommendRadioGroup[1].id)
            }
            comment.setText(show.comment)
        }
    }

    private fun deleteShow() {
        viewModel.removeShow(show)
        Toast.makeText(requireContext(), "Successfully deleted!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun showConfirmationDeleteDialog() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                // User clicked 'Yes'
                setPositiveButton(R.string.yes, DialogInterface.OnClickListener { _, _ ->
                    deleteShow()
                })

                // User clicked 'No'
                setNegativeButton(R.string.no, DialogInterface.OnClickListener { _, _ -> })
            }

            // Set other dialog properties
            builder.setMessage(R.string.delete_question).setTitle(R.string.delete_show)

            // Create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
    }
}
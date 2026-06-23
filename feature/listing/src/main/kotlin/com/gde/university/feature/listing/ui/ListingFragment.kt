package com.gde.university.feature.listing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gde.university.core.R
import com.gde.university.core.constants.NavigationConstants
import com.gde.university.feature.listing.mvi.ListingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingFragment : Fragment() {

    private val viewModel: ListingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ListingScreen(
                    viewModel = viewModel,
                    onNavigateToDetails = { university ->
                        val bundle = Bundle().apply {
                            putParcelable(NavigationConstants.ARG_UNIVERSITY, university)
                        }
                        // We will define the action in nav_graph
                        findNavController().navigate(
                            R.id.action_listing_to_details, bundle)
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Listen for refresh request from Details
        parentFragmentManager.setFragmentResultListener(NavigationConstants.REQUEST_KEY_REFRESH, viewLifecycleOwner) { _, _ ->
            viewModel.sendIntent(com.gde.university.feature.listing.mvi.ListingIntent.ForceRefreshData)
        }
    }
}

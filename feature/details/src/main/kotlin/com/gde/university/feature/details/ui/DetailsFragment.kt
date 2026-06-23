package com.gde.university.feature.details.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gde.university.core.constants.NavigationConstants
import com.gde.university.domain.model.UniversityModel
import com.gde.university.feature.details.R
import com.gde.university.feature.details.databinding.FragmentDetailsBinding
import com.gde.university.feature.details.mvi.DetailsEffect
import com.gde.university.feature.details.mvi.DetailsIntent
import com.gde.university.feature.details.mvi.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val universityModel = arguments?.let {
            BundleCompat.getParcelable(it, NavigationConstants.ARG_UNIVERSITY, UniversityModel::class.java)
        }
        universityModel?.let {
            viewModel.sendIntent(DetailsIntent.LoadDetails(it))
        }

        binding.buttonRefresh.setOnClickListener {
            viewModel.sendIntent(DetailsIntent.RefreshRequested)
        }

        observeState()
        observeEffect()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.universityModel?.let {
                    binding.textViewName.text = it.name
                    binding.textViewState.text = it.stateProvince ?: getString(R.string.n_a)
                    binding.textViewCountry.text = it.country
                    binding.textViewCode.text = it.alphaTwoCode
                    binding.textViewDomains.text = it.domains.joinToString(", ")
                    binding.textViewWebPages.text = it.webPages.joinToString("\n")
                }
            }
        }
    }

    private fun observeEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is DetailsEffect.CloseWithRefresh -> {
                        parentFragmentManager.setFragmentResult(NavigationConstants.REQUEST_KEY_REFRESH, Bundle())
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }
}

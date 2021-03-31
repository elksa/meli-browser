package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.elksa.sample.buscador.mercadolibre.databinding.FragmentProductDetailsBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProductDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailsViewModel by viewModels { viewModelFactory }
    private val args by navArgs<ProductDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater).apply {
            viewModel = this@ProductDetailsFragment.viewModel
            lifecycleOwner = this@ProductDetailsFragment
        }

        Toast.makeText(requireContext(), args.product.title, Toast.LENGTH_SHORT).show()

        return binding.root
    }

}
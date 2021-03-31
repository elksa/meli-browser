package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.elksa.sample.buscador.mercadolibre.databinding.FragmentProductsListBinding
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductsListFragmentDirections.Companion.actionDestProductsListFragmentToDestProductDetailsFragment
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.CustomListAdapter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.ListItemDataAbstract
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ProductsListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding1: FragmentProductsListBinding
    private val viewModel: ProductsListViewModel by viewModels { viewModelFactory }
    private val adapter = CustomListAdapter { parent, _ ->
        ProductItemView(
            parent.context,
            viewModel::onProductSelected
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding1 = FragmentProductsListBinding.inflate(inflater).apply {
            viewModel = this@ProductsListFragment.viewModel
            lifecycleOwner = this@ProductsListFragment
            rvProductsListResults.adapter = adapter
        }

        setupObservers()
        viewModel.searchProducts("Motorola")

        return binding1.root
    }

    private fun setupObservers() {
        with(viewModel) {
            productsList.observe(viewLifecycleOwner, {
                adapter.submitList(
                    it.map { productUiModel ->
                        ListItemDataAbstract(productUiModel)
                    }
                )
            })
            productDetailsNavigationEvent.observe(viewLifecycleOwner, {
                it?.let { product ->
                    navigateToProductDetails(product)
                    productDetailsNavigationEventConsumed()
                }
            })
        }
    }

    private fun navigateToProductDetails(product: ProductUiModel) {
        findNavController().navigate(
            actionDestProductsListFragmentToDestProductDetailsFragment(product)
        )
    }

}
package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
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

    private lateinit var binding: FragmentProductsListBinding
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
        binding = FragmentProductsListBinding.inflate(inflater).apply {
            viewModel = this@ProductsListFragment.viewModel
            lifecycleOwner = this@ProductsListFragment
            rvProductsListResults.adapter = adapter
        }

        setupObservers()
        viewModel.init()

        val searchManager = requireActivity().getSystemService(SEARCH_SERVICE) as SearchManager
        binding.searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            isSubmitButtonEnabled = true
            isQueryRefinementEnabled = true
        }

        return binding.root
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
            hideKeyboardEvent.observe(viewLifecycleOwner, {
                if (it == true) {
                    binding.searchView.clearFocus()
                    viewModel.hideKeyboardEventConsumed()
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
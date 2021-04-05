package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.elksa.sample.buscador.mercadolibre.databinding.FragmentProductsListBinding
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.BaseDaggerFragment
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.CustomListAdapter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.ListItemDataAbstract
import javax.inject.Inject

class ProductsListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisibleItems = 0

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
            rvProductsListResults.addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        rvProductsListResults.layoutManager?.let {
                            visibleItemCount = it.childCount
                            totalItemCount = it.itemCount
                            pastVisibleItems =
                                (it as LinearLayoutManager).findFirstVisibleItemPosition()
                        }
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            if (binding.srlProductsListResults.isRefreshing.not()) {
                                this@ProductsListFragment.viewModel.searchProducts()
                            }
                        }
                    }
                }
            })
        }

        setupObservers()
        viewModel.init()

        val searchManager = requireActivity().getSystemService(SEARCH_SERVICE) as SearchManager
        binding.svProductsListSearch.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            isSubmitButtonEnabled = true
            isQueryRefinementEnabled = true
        }

        return binding.root
    }

    private fun setupObservers() {
        viewModel.run {
            observerViewModelEvents(this)
            productsList.observe(viewLifecycleOwner, {
                adapter.submitList(
                    it.map { productUiModel -> ListItemDataAbstract(productUiModel) }
                )
            })
            hideKeyboardEvent.observe(viewLifecycleOwner, {
                if (it == true) binding.svProductsListSearch.clearFocus()
            })
            errorEvent.observe(viewLifecycleOwner, { dialogInfo ->
                dialogInfo?.let { showDialog(it) }
            })
            deleteRecentSearchesEvent.observe(viewLifecycleOwner, { dialogModel ->
                dialogModel?.let { showDialog(it) }
            })
        }
    }
}
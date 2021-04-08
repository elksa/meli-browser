package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.databinding.FragmentProductsListBinding
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.BaseDaggerFragment
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.CustomListAdapter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.ListItemDataAbstract
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.IImageLoader
import javax.inject.Inject

class ProductsListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var imageLoader: IImageLoader<*>

    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisibleItems = 0
    private lateinit var searchView: SearchView

    private lateinit var binding: FragmentProductsListBinding
    private val viewModel: ProductsListViewModel by viewModels { viewModelFactory }
    private val adapter = CustomListAdapter { parent, _ ->
        ProductItemView(
            parent.context,
            imageLoader,
            viewModel::onProductSelected
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().invalidateOptionsMenu()
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(binding.layoutProductsListToolbar.tbAppBar)
        }
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
                                this@ProductsListFragment.viewModel.doSearch()
                            }
                        }
                    }
                }
            })
        }

        setupObservers()
        viewModel.init()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_list, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        val searchManager = requireActivity().getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = (searchViewItem.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            isSubmitButtonEnabled = true
            isQueryRefinementEnabled = true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_delete_searches -> viewModel.onDeleteRecentSearches()
        }
        return super.onOptionsItemSelected(item)
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
                if (it == true && ::searchView.isInitialized) searchView.clearFocus()
            })
            errorEvent.observe(viewLifecycleOwner, { dialogInfo ->
                dialogInfo?.let { showDialog(it) }
            })
            deleteRecentSearchesEvent.observe(viewLifecycleOwner, { dialogModel ->
                dialogModel?.let { showDialog(it) }
            })
        }
    }

    override fun onDestroyView() {
        binding.rvProductsListResults.adapter = null
        super.onDestroyView()
    }
}
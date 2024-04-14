package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.databinding.FragmentProductDetailsBinding
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.CustomListAdapter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.ListItemDataAbstract
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.BaseFragment
import com.elksa.sample.buscador.mercadolibre.presentation.ui.theme.MeliTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailsViewModel by viewModels()
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private val adapter by lazy {
        CustomListAdapter { parent, _ -> ProductItemView(parent.context) }
    }

    override fun getCurrentView() = binding.root

    override fun initComponents(inflater: LayoutInflater) {
        binding = FragmentProductDetailsBinding.inflate(inflater).apply {
            viewModel = this@ProductDetailsFragment.viewModel
            lifecycleOwner = this@ProductDetailsFragment
            composeView.apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
                setContent {
                    MeliTheme {
                        ProductDetails(viewModel!!, adapter)
                    }
                }
            }
        }
        viewModel.init(args.product)
    }

    override fun setupActionBar() {
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(binding.layoutProductsListToolbar.tbAppBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.layoutProductsListToolbar.tbAppBar.run {
            setNavigationOnClickListener { viewModel.onBackPressed() }
            setTitle(R.string.title_product_details)
        }
    }

    override fun setupObservers() {
        viewModel.run {
            observerViewModelEvents(this)
            productDetails.observe(viewLifecycleOwner) {
                adapter.submitList(
                    it.pictures.map { pictureUiModel -> ListItemDataAbstract(pictureUiModel) }
                )
            }
            errorEvent.observe(viewLifecycleOwner) { dialogInfo ->
                dialogInfo?.let { showDialog(it) }
            }
        }
    }
}
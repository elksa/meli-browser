package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.elksa.sample.buscador.mercadolibre.databinding.FragmentProductDetailsBinding
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.CustomListAdapter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.ListItemDataAbstract
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.BaseDaggerFragment
import javax.inject.Inject

class ProductDetailsFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailsViewModel by viewModels { viewModelFactory }
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private val adapter = CustomListAdapter { parent, _ ->
        ProductItemView(parent.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater).apply {
            viewModel = this@ProductDetailsFragment.viewModel
            lifecycleOwner = this@ProductDetailsFragment
        }

        viewModel.init(args.product)
        setupObservers()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener { viewModel.onBackPressed() }
    }


    private fun setupObservers() {
        viewModel.run {
            observerViewModelEvents(this)
            productDetails.observe(viewLifecycleOwner, {
                setupPicturesPager()
                adapter.submitList(
                    it.pictures.map { pictureUiModel -> ListItemDataAbstract(pictureUiModel) }
                )
            })
            errorEvent.observe(viewLifecycleOwner, { dialogInfo ->
                dialogInfo?.let { showDialog(it) }
            })
        }
    }

    private fun setupPicturesPager() {
        binding.pagerProductDetailsPictures.run {
            orientation = ORIENTATION_HORIZONTAL
            adapter = this@ProductDetailsFragment.adapter
            setPageTransformer(ZoomOutTransformation())
            registerOnPageChangeCallback(object : OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    viewModel.updatePageIndicator(position)
                }
            })
        }
    }
}
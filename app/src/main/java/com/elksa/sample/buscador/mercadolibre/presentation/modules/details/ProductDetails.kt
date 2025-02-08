package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.view.View
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity.ItemCondition
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductUiModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.CustomListAdapter
import com.elksa.sample.buscador.mercadolibre.ui.carousel.CarouselView
import com.elksa.sample.buscador.mercadolibre.ui.carousel.setIndicatorData
import com.elksa.sample.buscador.mercadolibre.ui.carousel.setIndicatorDefaultImageFromUrl
import com.elksa.sample.buscador.mercadolibre.ui.carousel.showIndicatorThumbnail

@Composable
fun ProductDetails(viewModel: ProductDetailsViewModel, adapter: CustomListAdapter) {

    val item by viewModel.product.observeAsState()
    val details by viewModel.productDetails.observeAsState()
    val loaderVisibility by viewModel.loaderVisibility.observeAsState()

    item?.let {
        Surface {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.activity_default_margin))
                    .fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ItemCondition(it)
                    Spacer(modifier = Modifier.weight(1f))
                    ItemShipping(it)
                }
                PicturesCarousel(adapter, viewModel)

                if (loaderVisibility == View.VISIBLE) {
                    CircularProgressAnimated(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                ItemTitle(title = it.title)
                ItemPrice(price = it.price)
                ItemDescription(description = details?.description ?: "")
            }
        }
    }
}

@Composable
private fun ItemTitle(title: String) {
    Text(
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.spacing_small)),
        text = title,
        maxLines = 2,
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun ItemPrice(price: String) {
    Text(
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.spacing_xsmall)),
        text = price,
        style = MaterialTheme.typography.displaySmall
    )
}

@Composable
private fun ItemDescription(description: String) {
    Column {
        Text(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.spacing_small)),
            text = stringResource(id = R.string.label_description),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.spacing_small)),
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ItemCondition(item: ProductUiModel) {
    val condition = stringResource(
        R.string.format_details_item_condition_units_sold,
        stringResource(id = getItemCondition(item)),
        item.soldQuantity
    )
    Text(
        text = condition,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun ItemShipping(item: ProductUiModel) {
    val colorRes = if (item.freeShipping) R.color.freeGreenColor else R.color.primaryDarkColor
    val shippingRes =
        if (item.freeShipping) R.string.label_shipment_free else R.string.label_shipment_not_free
    Text(
        text = stringResource(id = shippingRes),
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.spacing_xxsmall),
                end = dimensionResource(id = R.dimen.spacing_xxsmall)
            )
            .background(
                color = colorResource(id = colorRes),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.radius_xsmall))
            ),
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
private fun PicturesCarousel(adapter: CustomListAdapter, viewModel: ProductDetailsViewModel) {
    val item by viewModel.product.observeAsState()
    val currentPicturePosition by viewModel.currentPicturePosition.observeAsState()
    val details by viewModel.productDetails.observeAsState()
    val isThumbnailVisible by viewModel.isThumbnailVisible.observeAsState()

    AndroidView(
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.spacing_small))
            .fillMaxWidth(),
        factory = { context ->
            CarouselView(context).apply {
                setupCarousel(
                    orientation = ORIENTATION_HORIZONTAL,
                    adapter = adapter,
                    onPageChangeCallback = viewModel::updatePageIndicator,
                    pageTransformer = ZoomOutTransformation()
                )
            }
        },
        update = {
            it.apply {
                setIndicatorData(
                    indicatorItemCount = details?.pictures?.size,
                    indicatorCurrentPage = currentPicturePosition
                )
                setIndicatorDefaultImageFromUrl(item?.thumbnail)
                showIndicatorThumbnail(isThumbnailVisible)
            }
        }
    )
}

@Composable
private fun CircularProgressAnimated(modifier: Modifier = Modifier) {

    val label = "progressInfiniteTransition"
    val animationDuration = 900
    val progressValue = 0.75f
    val infiniteTransition = rememberInfiniteTransition(label = label)

    val progressAnimationValue by infiniteTransition.animateFloat(
        label = label,
        initialValue = 0.0f,
        targetValue = progressValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration)
        )
    )

    CircularProgressIndicator(
        progress = {
            progressAnimationValue
        },
        modifier = modifier
            .padding(all = dimensionResource(id = R.dimen.spacing_medium)),
    )
}

private fun getItemCondition(item: ProductUiModel) = when (item.condition) {
    ItemCondition.NEW -> R.string.label_condition_new
    ItemCondition.USED -> R.string.label_condition_used
    ItemCondition.NOT_SPECIFIED -> R.string.label_condition_not_specified
}
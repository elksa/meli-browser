package com.elksa.sample.buscador.mercadolibre.ui.carousel

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import com.elksa.sample.buscador.mercadolibre.ui.R
import com.elksa.sample.buscador.mercadolibre.ui.common.imageLoader.GlideImageLoader
import com.elksa.sample.buscador.mercadolibre.ui.common.isVisible

private const val DEFAULT_ITEM_COUNT = 0
private const val DEFAULT_ITEM_INDEX = -1

class CarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val hMargin: Int

    private val pagerCarousel: ViewPager2
    private val imgThumbnail: ImageView
    private val viewIndicator: LinearLayout

    private var itemCount: Int
    private var currentItemIndex: Int
    private var thumbnailVisible: Boolean

    init {
        inflate(context, R.layout.layout_carousel, this)

        hMargin = resources.getDimension(R.dimen.ui_indicator_horizontal_margin).toInt()
        pagerCarousel = findViewById(R.id.pager_carousel_pictures)
        imgThumbnail = findViewById(R.id.img_carousel_thumbnail)
        viewIndicator = findViewById(R.id.layout_carousel_indicator)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CarouselView)

        itemCount = attributes.getInt(R.styleable.CarouselView_itemCount, DEFAULT_ITEM_COUNT)
        currentItemIndex =
            attributes.getInt(R.styleable.CarouselView_currentItemIndex, DEFAULT_ITEM_INDEX)
        thumbnailVisible =
            attributes.getBoolean(R.styleable.CarouselView_isThumbnailVisible, false)

        updateItemCount()
        updateCurrentPage()

        attributes.recycle()
    }

    fun setItemCount(itemCount: Int) {
        this.itemCount = itemCount
        updateItemCount()
    }

    fun setCurrentItemIndex(currentItemIndex: Int) {
        this.currentItemIndex = currentItemIndex
        updateCurrentPage()
    }

    fun setIndicatorDefaultImageUrl(urlString: String) {
        GlideImageLoader(imgThumbnail.context).loadImage(urlString, imgThumbnail)
    }

    fun setThumbnailVisible(thumbnailVisible: Boolean) {
        this.thumbnailVisible = thumbnailVisible
        imgThumbnail.isVisible(thumbnailVisible)
    }

    fun setupCarousel(
        orientation: Int,
        adapter: Adapter<*>,
        onPageChangeCallback: ((Int) -> Unit)? = null,
        pageTransformer: PageTransformer? = null
    ) {
        pagerCarousel.run {
            this.orientation = orientation
            this.adapter = adapter
            onPageChangeCallback?.let {
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                    override fun onPageSelected(position: Int) {
                        it(position)
                    }
                })
            }
            pageTransformer?.let { setPageTransformer(it) }
        }
    }

    private fun updateItemCount() {
        viewIndicator.run {
            if (childCount == 0) {
                for (i in 1..itemCount) {
                    addView(
                        ImageView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(hMargin, 0, hMargin, 0)
                            }
                            setImageResource(R.drawable.ic_circle_outlined)
                        }
                    )
                }
            }
        }
    }

    private fun updateCurrentPage() {
        viewIndicator.run {
            for (i in 0 until childCount) {
                (getChildAt(i) as ImageView).run {
                    this.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(hMargin, 0, hMargin, 0)
                    }
                    setImageResource(
                        if (currentItemIndex == i) R.drawable.ic_circle_filled
                        else R.drawable.ic_circle_outlined
                    )
                }
            }
        }
    }
}

@BindingAdapter(value = ["indicatorItemCount", "indicatorCurrentPage"])
fun CarouselView.setIndicatorData(indicatorItemCount: Int?, indicatorCurrentPage: Int?) {
    indicatorItemCount?.let { setItemCount(it) }
    indicatorCurrentPage?.let { setCurrentItemIndex(it) }
}

@BindingAdapter("indicatorDefaultImageUrl")
fun CarouselView.setIndicatorDefaultImageFromUrl(url: String?) {
    url?.let { setIndicatorDefaultImageUrl(it) }
}

@BindingAdapter("indicatorThumbnailVisible")
fun CarouselView.showIndicatorThumbnail(isVisible: Boolean?) {
    isVisible?.let { setThumbnailVisible(it) }
}
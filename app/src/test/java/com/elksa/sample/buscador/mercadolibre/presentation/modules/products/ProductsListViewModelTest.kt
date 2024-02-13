package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.domain.interactors.ClearRecentSuggestionsUseCase
import com.elksa.sample.buscador.mercadolibre.domain.interactors.FetchProductRecommendationsUseCase
import com.elksa.sample.buscador.mercadolibre.domain.interactors.SearchProductsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductsListFragmentDirections.Companion.actionDestProductsListFragmentToDestProductDetailsFragment
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBusListener
import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.MoneyFormatter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.DialogInfoUiModel
import com.elksa.sample.buscador.mercadolibre.utils.TestScheduler
import com.elksa.sample.buscador.mercadolibre.utils.callPrivateFun
import com.elksa.sample.buscador.mercadolibre.utils.getField
import com.elksa.sample.buscador.mercadolibre.utils.getProductUiModelFromProductEntity
import com.elksa.sample.buscador.mercadolibre.utils.getSampleProducts
import com.elksa.sample.buscador.mercadolibre.utils.setField
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any

private const val PAGE_SIZE = 50
private const val PAGE_SIZE_RECOMMENDATIONS = 15
private const val TAG = "ProductsListViewModel"
private const val FIELD_NAME_COMPOSITE_DISPOSABLE = "compositeDisposable"
private const val FIELD_NAME_PRODUCTS_LIST = "_productsList"
private const val FIELD_NAME_QUERY = "query"
private const val FIELD_NAME_DELETE_RECENT_SEARCHES = "deleteRecentSearches"
private const val FUNCTION_NAME_ON_CLEARED = "onCleared"

@RunWith(MockitoJUnitRunner::class)
class ProductsListViewModelTest {

    private lateinit var sut: ProductsListViewModel
    private val testScheduler = TestScheduler()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var searchProductsUseCaseMock: SearchProductsUseCase

    @Mock
    private lateinit var loggerMock: ILogger

    @Mock
    private lateinit var eventBusListenerMock: IEventBusListener

    @Mock
    private lateinit var moneyFormatterMock: MoneyFormatter

    @Mock
    private lateinit var compositeDisposableMock: CompositeDisposable

    @Mock
    private lateinit var fetchProductRecommendationsUseCaseMock: FetchProductRecommendationsUseCase

    @Mock
    private lateinit var clearRecentSuggestionsUseCaseMock: ClearRecentSuggestionsUseCase

    @Before
    fun setUp() {
        sut = ProductsListViewModel(
            searchProductsUseCaseMock,
            fetchProductRecommendationsUseCaseMock,
            clearRecentSuggestionsUseCaseMock,
            testScheduler,
            loggerMock,
            eventBusListenerMock,
            moneyFormatterMock
        )
    }

    @Test
    fun doSearch_onSuccessSearchResultsWithResults_productsSetLoaderGoneEmptyInfoGone() {
        // given
        val products = getSampleProducts()
        `when`(searchProductsUseCaseMock.searchProducts(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.just(products))
        `when`(moneyFormatterMock.format(any<Number>())).thenReturn(EMPTY_STRING)
        setField(FIELD_NAME_QUERY, EMPTY_STRING, sut)
        // when
        sut.doSearch()
        // then
        assertEquals(products.size, sut.productsList.value?.size)
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(false, sut.iseEmptySearchVisible.value)
    }

    @Test
    fun doSearch_onSuccessSearchProductsSuccessWithResults_productsProperlyMapped() {
        // given
        val products = getSampleProducts()
        val formattedPrice = "formattedPrice"
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.just(products))
        `when`(moneyFormatterMock.format(any())).thenReturn(formattedPrice)
        val expectedProductUiModel = getProductUiModelFromProductEntity(products[0], formattedPrice)
        setField(FIELD_NAME_QUERY, EMPTY_STRING, sut)
        // when
        sut.doSearch()
        // then
        assertEquals(products.size, sut.productsList.value?.size)
        assertEquals(expectedProductUiModel, sut.productsList.value?.get(0))
    }

    @Test
    fun doSearch_onSuccessSearchProductsNoResults_productsEmptyLoaderGoneEmptyInfoVisible() {
        // given
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.just(listOf()))
        setField(FIELD_NAME_QUERY, EMPTY_STRING, sut)
        // when
        sut.doSearch()
        // then
        assertEquals(true, sut.productsList.value?.isEmpty())
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(true, sut.iseEmptySearchVisible.value)
    }

    @Test
    fun doSearch_onSuccessSearchProductsNoResultsWithPreviousProducts_emptyInfoGone() {
        // given
        val products = listOf(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(products), sut)
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.just(listOf()))
        setField(FIELD_NAME_QUERY, EMPTY_STRING, sut)
        // when
        sut.doSearch()
        // then
        assertEquals(false, sut.iseEmptySearchVisible.value)
    }

    @Test
    fun doSearch_onFailureSearchProductsNoPreviousProducts_emptyInfoVisibleLoaderGoneErrorLoggedAndShown() {
        // given
        val error = Throwable("error loading products")
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(error))
        val errorInfo = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_products_search
        )
        setField(FIELD_NAME_QUERY, EMPTY_STRING, sut)
        // when
        sut.doSearch()
        // then
        verify(loggerMock).log(TAG, error.toString(), error, ERROR)
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(true, sut.iseEmptySearchVisible.value)
        assertEquals(errorInfo, sut.errorEvent.value)
    }

    @Test
    fun doSearch_onFailureSearchProductsWithPreviousProducts_emptyInfoGoneLoaderGoneErrorLoggedAndShown() {
        // given
        val products = listOf(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(products), sut)
        val error = Throwable("error loading products")
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(error))
        setField(FIELD_NAME_QUERY, EMPTY_STRING, sut)
        val errorInfo = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_products_search
        )
        // when
        sut.doSearch()
        // then
        assertEquals(false, sut.iseEmptySearchVisible.value)

        verify(loggerMock).log(TAG, error.toString(), error, ERROR)
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(false, sut.iseEmptySearchVisible.value)
        assertEquals(errorInfo, sut.errorEvent.value)
    }

    @Test
    fun doSearch_searchProducts_hideKeyboardEventTriggered() {
        // given
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(Throwable()))
        setField(FIELD_NAME_QUERY, EMPTY_STRING, sut)
        // when
        sut.doSearch()
        // then
        assertEquals(true, sut.hideKeyboardEvent.value)
    }

    @Test
    fun doSearch_searchProductsWithPreviousProducts_searchProductsUseCaseProperlyInvoked() {
        // given
        val query = "query"
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(Throwable()))
        val products = listOf(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(products), sut)
        setField(FIELD_NAME_QUERY, query, sut)
        // when
        sut.doSearch()
        // then
        verify(searchProductsUseCaseMock).searchProducts(query, products.size, PAGE_SIZE)
    }

    @Test
    fun doSearch_onSearchProductsSuccessResultsWithPreviousResults_productsConcatenated() {
        // given
        val products = getSampleProducts()
        val formattedPrice = "formattedPrice"
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.just(products))
        `when`(moneyFormatterMock.format(any())).thenReturn(formattedPrice)
        val expectedProductUiModel1 =
            getProductUiModelFromProductEntity(products[0], formattedPrice)
        val expectedProductUiModel2 =
            getProductUiModelFromProductEntity(products[0], formattedPrice)
        val existingProducts = listOf(expectedProductUiModel2)
        setField(FIELD_NAME_QUERY, EMPTY_STRING, sut)
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(existingProducts), sut)
        // when
        sut.doSearch()
        // then
        assertEquals(products.size + existingProducts.size, sut.productsList.value?.size)
        assertEquals(expectedProductUiModel1, sut.productsList.value?.get(0))
        assertEquals(expectedProductUiModel2, sut.productsList.value?.get(1))
    }

    @Test
    fun doSearch_searchProductsNoPreviousProducts_searchProductsUseCaseyInvokedOffsetZero() {
        // given
        val query = "query"
        `when`(
            searchProductsUseCaseMock.searchProducts(
                anyString(),
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(Throwable()))
        setField(FIELD_NAME_QUERY, query, sut)
        // when
        sut.doSearch()
        // then
        verify(searchProductsUseCaseMock).searchProducts(query, 0, PAGE_SIZE)
    }

    @Test
    fun doSearch_onFetchRecommendationsSuccessWithResults_productsProperlyMapped() {
        // given
        val products = getSampleProducts()
        val formattedPrice = "formattedPrice"
        `when`(
            fetchProductRecommendationsUseCaseMock.fetchProductRecommendations(
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.just(products))
        `when`(moneyFormatterMock.format(any())).thenReturn(formattedPrice)
        val expectedProductUiModel = getProductUiModelFromProductEntity(products[0], formattedPrice)
        setField(FIELD_NAME_QUERY, null, sut)
        // when
        sut.doSearch()
        // then
        assertEquals(products.size, sut.productsList.value?.size)
        assertEquals(expectedProductUiModel, sut.productsList.value?.get(0))
    }

    @Test
    fun doSearch_onFetchRecommendationsNoResults_productsListEmptyLoaderGoneEmptyInfoVisible() {
        // given
        `when`(
            fetchProductRecommendationsUseCaseMock.fetchProductRecommendations(
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.just(listOf()))
        setField(FIELD_NAME_QUERY, null, sut)
        // when
        sut.doSearch()
        // then
        assertEquals(true, sut.productsList.value?.isEmpty())
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(true, sut.iseEmptySearchVisible.value)
    }

    @Test
    fun doSearch_onFetchRecommendationsSuccessResultsWithPreviousResults_productsConcatenated() {
        // given
        val products = getSampleProducts()
        val formattedPrice = "formattedPrice"
        `when`(
            fetchProductRecommendationsUseCaseMock.fetchProductRecommendations(
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.just(products))
        `when`(moneyFormatterMock.format(any())).thenReturn(formattedPrice)
        val expectedProductUiModel1 =
            getProductUiModelFromProductEntity(products[0], formattedPrice)
        val expectedProductUiModel2 =
            getProductUiModelFromProductEntity(products[0], formattedPrice)
        val existingProducts = listOf(expectedProductUiModel2)
        setField(FIELD_NAME_QUERY, null, sut)
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(existingProducts), sut)
        // when
        sut.doSearch()
        // then
        assertEquals(products.size + existingProducts.size, sut.productsList.value?.size)
        assertEquals(expectedProductUiModel1, sut.productsList.value?.get(0))
        assertEquals(expectedProductUiModel2, sut.productsList.value?.get(1))
    }

    @Test
    fun doSearch_onFetchRecommendationsFailureNoPreviousResults_listEmptyLoaderGoneEmptyInfoVisibleErrorLoggedAndShowed() {
        // given
        val error = Throwable("error fetching recommendations")
        `when`(
            fetchProductRecommendationsUseCaseMock.fetchProductRecommendations(
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(error))
        setField(FIELD_NAME_QUERY, null, sut)
        // when
        sut.doSearch()
        val errorInfo = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_products_search
        )
        // then
        assertEquals(true, sut.productsList.value?.isNullOrEmpty())
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(true, sut.iseEmptySearchVisible.value)
        assertEquals(errorInfo, sut.errorEvent.value)
    }

    @Test
    fun doSearch_onFetchRecommendationsFailurePreviousResults_listNotEmptyLoaderGoneEmptyInfoGoneErrorLoggedAndShowed() {
        // given
        val error = Throwable("error fetching recommendations")
        `when`(
            fetchProductRecommendationsUseCaseMock.fetchProductRecommendations(
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(error))
        setField(FIELD_NAME_QUERY, null, sut)
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(getSampleProducts()), sut)
        // when
        sut.doSearch()
        val errorInfo = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_products_search
        )
        // then
        assertEquals(false, sut.productsList.value?.isEmpty())
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(false, sut.iseEmptySearchVisible.value)
        assertEquals(errorInfo, sut.errorEvent.value)
    }

    @Test
    fun doSearch_fetchRecommendationsWithPreviousProducts_fetchRecommendationsUseCaseProperlyInvoked() {
        // given
        `when`(
            fetchProductRecommendationsUseCaseMock.fetchProductRecommendations(
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(Throwable()))
        val products = listOf(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(products), sut)
        // when
        sut.doSearch()
        // then
        verify(fetchProductRecommendationsUseCaseMock).fetchProductRecommendations(
            products.size,
            PAGE_SIZE_RECOMMENDATIONS
        )
    }

    @Test
    fun doSearch_fetchRecommendationsNoPreviousProducts_searchProductsUseCaseyInvokedOffsetZero() {
        // given
        `when`(
            fetchProductRecommendationsUseCaseMock.fetchProductRecommendations(
                anyInt(),
                anyInt()
            )
        ).thenReturn(Single.error(Throwable()))
        // when
        sut.doSearch()
        // then
        verify(fetchProductRecommendationsUseCaseMock).fetchProductRecommendations(
            0,
            PAGE_SIZE_RECOMMENDATIONS
        )
    }

    @Test
    fun onProductSelected_productSelected_navigateToProductDetailsEventTriggered() {
        // given
        val selectedProduct = getProductUiModelFromProductEntity(getSampleProducts()[0])
        val expectedEvent = NavigationToDirectionEvent(
            actionDestProductsListFragmentToDestProductDetailsFragment(selectedProduct)
        )
        // when
        sut.onProductSelected(selectedProduct)
        // then
        assertEquals(expectedEvent, sut.navigationEvent.value)
    }

    @Test
    fun onCleared_invoked_disposableCleared() {
        // given
        setField(FIELD_NAME_COMPOSITE_DISPOSABLE, compositeDisposableMock, sut)
        // when
        sut.callPrivateFun(FUNCTION_NAME_ON_CLEARED)
        // then
        verify(compositeDisposableMock).clear()
    }

    @Test
    fun onDeleteRecentSearches_invoked_deleteRecentSearchesEventTriggered() {
        // given
        val info = DialogInfoUiModel(
            R.drawable.ic_help,
            R.string.title_dialog_delete_recent_searches,
            R.string.message_dialog_delete_recent_searches,
            android.R.string.ok,
            getField(sut, FIELD_NAME_DELETE_RECENT_SEARCHES),
            android.R.string.cancel
        )
        // when
        sut.onDeleteRecentSearches()
        // then
        assertEquals(info, sut.deleteRecentSearchesEvent.value)
    }
}
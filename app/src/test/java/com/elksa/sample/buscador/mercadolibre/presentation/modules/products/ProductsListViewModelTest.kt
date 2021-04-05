package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.provider.SearchRecentSuggestions
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.ProductsSearchResultEntity
import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.interactors.SearchProductsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.DialogInfoUiModel
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductsListFragmentDirections.Companion.actionDestProductsListFragmentToDestProductDetailsFragment
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.MoneyFormatter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import com.elksa.sample.buscador.mercadolibre.utils.TestScheduler
import com.elksa.sample.buscador.mercadolibre.utils.callPrivateFun
import com.elksa.sample.buscador.mercadolibre.utils.getField
import com.elksa.sample.buscador.mercadolibre.utils.getProductUiModelFromProductEntity
import com.elksa.sample.buscador.mercadolibre.utils.getSampleProducts
import com.elksa.sample.buscador.mercadolibre.utils.setField
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
import org.mockito.junit.MockitoJUnitRunner

private const val PAGE_SIZE = 50
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
    private lateinit var eventBusMock: IEventBus

    @Mock
    private lateinit var moneyFormatterMock: MoneyFormatter

    @Mock
    private lateinit var compositeDisposableMock: CompositeDisposable

    @Mock
    private lateinit var searchRecentSuggestionsMock: SearchRecentSuggestions

    @Before
    fun setUp() {
        sut = ProductsListViewModel(
            searchProductsUseCaseMock,
            testScheduler,
            loggerMock,
            eventBusMock,
            moneyFormatterMock,
            searchRecentSuggestionsMock
        )
    }

    @Test
    fun getProductsList_onSuccessWithResults_productsSetLoaderGoneEmptyInfoGone() {
        // given
        val products = getSampleProducts()
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.just(ProductsSearchResultEntity(EMPTY_STRING, products)))
        whenever(moneyFormatterMock.format(any())).thenReturn(EMPTY_STRING)
        // when
        sut.searchProducts()
        // then
        assertEquals(products.size, sut.productsList.value?.size)
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(false, sut.iseEmptySearchVisible.value)
    }

    @Test
    fun getProductsList_onSuccessWithResults_productsProperlyMapped() {
        // given
        val products = getSampleProducts()
        val formattedPrice = "formattedPrice"
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.just(ProductsSearchResultEntity(EMPTY_STRING, products)))
        whenever(moneyFormatterMock.format(any())).thenReturn(formattedPrice)
        val expectedProductUiModel = getProductUiModelFromProductEntity(products[0], formattedPrice)
        // when
        sut.searchProducts()
        // then
        assertEquals(products.size, sut.productsList.value?.size)
        assertEquals(expectedProductUiModel, sut.productsList.value?.get(0))
    }

    @Test
    fun getProductsList_onSuccessNoResults_productsEmptyLoaderGoneEmptyInfoVisible() {
        // given
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.just(ProductsSearchResultEntity(EMPTY_STRING, listOf())))
        // when
        sut.searchProducts()
        // then
        assertEquals(true, sut.productsList.value?.isEmpty())
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(true, sut.iseEmptySearchVisible.value)
    }

    @Test
    fun getProductsList_onSuccessNoResultsWithPreviousProducts_emptyInfoGone() {
        // given
        val products = listOf(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(products), sut)
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.just(ProductsSearchResultEntity(EMPTY_STRING, listOf())))
        // when
        sut.searchProducts()
        // then
        assertEquals(false, sut.iseEmptySearchVisible.value)
    }

    @Test
    fun getProductsList_onFailure_emptyInfoVisibleLoaderGoneErrorLoggedAndShown() {
        // given
        val error = Throwable("error loading products")
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.error(error))
        val info = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_products_search
        )
        // when
        sut.searchProducts()
        // then
        verify(loggerMock).log(TAG, error.toString(), error, ERROR)
        assertEquals(false, sut.isLoaderVisible.value)
        assertEquals(true, sut.iseEmptySearchVisible.value)
        assertEquals(info, sut.errorEvent.value)
    }

    @Test
    fun getProductsList_onFailurePreviousProducts_emptyInfoGone() {
        // given
        val products = listOf(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(products), sut)
        val error = Throwable("error loading products")
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.error(error))
        // when
        sut.searchProducts()
        // then
        assertEquals(false, sut.iseEmptySearchVisible.value)
    }

    @Test
    fun getProductsList_invocation_hideKeyboardEventTriggered() {
        // given
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.error(Throwable()))
        // when
        sut.searchProducts()
        // then
        assertEquals(true, sut.hideKeyboardEvent.value)
    }

    @Test
    fun getProductsList_invocationPreviousProducts_searchProductsUseCaseProperlyInvoked() {
        // given
        val query = "query"
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.error(Throwable()))
        val products = listOf(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        setField(FIELD_NAME_PRODUCTS_LIST, MutableLiveData(products), sut)
        setField(FIELD_NAME_QUERY, query, sut)
        // when
        sut.searchProducts()
        // then
        verify(searchProductsUseCaseMock).searchProducts(query, products.size, PAGE_SIZE)
    }

    @Test
    fun getProductsList_invocationNoPreviousProducts_searchProductsUseCaseyInvokedOffsetZero() {
        // given
        val query = "query"
        whenever(searchProductsUseCaseMock.searchProducts(
            anyString(),
            anyInt(),
            anyInt()
        )).thenReturn(Single.error(Throwable()))
        setField(FIELD_NAME_QUERY, query, sut)
        // when
        sut.searchProducts()
        // then
        verify(searchProductsUseCaseMock).searchProducts(query, 0, PAGE_SIZE)
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
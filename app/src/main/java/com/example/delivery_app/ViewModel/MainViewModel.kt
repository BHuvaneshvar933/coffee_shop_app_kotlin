package com.example.delivery_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery_app.domain.BannerModel
import com.example.delivery_app.domain.CategoryModel
import com.example.delivery_app.domain.ItemsModel
import com.example.delivery_app.repository.MainRepository
import com.example.delivery_app.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _bannerState = MutableStateFlow<UiState<List<BannerModel>>>(UiState.Loading)
    val bannerState: StateFlow<UiState<List<BannerModel>>> = _bannerState

    private val _categoryState = MutableStateFlow<UiState<List<CategoryModel>>>(UiState.Loading)
    val categoryState: StateFlow<UiState<List<CategoryModel>>> = _categoryState

    private val _popularState = MutableStateFlow<UiState<List<ItemsModel>>>(UiState.Loading)
    val popularState: StateFlow<UiState<List<ItemsModel>>> = _popularState

    private val _restaurantState = MutableStateFlow<UiState<List<com.example.delivery_app.domain.RestaurantModel>>>(UiState.Loading)
    val restaurantState: StateFlow<UiState<List<com.example.delivery_app.domain.RestaurantModel>>> = _restaurantState

    private val _ordersState = MutableStateFlow<UiState<List<com.example.delivery_app.domain.OrderModel>>>(UiState.Loading)
    val ordersState: StateFlow<UiState<List<com.example.delivery_app.domain.OrderModel>>> = _ordersState

    fun loadBanner() {
        viewModelScope.launch {
            _bannerState.value = UiState.Loading
            repository.loadBanner()
                .catch { e -> _bannerState.value = UiState.Error(e.message ?: "Unknown Error") }
                .collect { items ->
                    _bannerState.value = UiState.Success(items)
                }
        }
    }

    fun loadCategory() {
        viewModelScope.launch {
            _categoryState.value = UiState.Loading
            repository.loadCategory()
                .catch { e -> _categoryState.value = UiState.Error(e.message ?: "Unknown Error") }
                .collect { items ->
                    _categoryState.value = UiState.Success(items)
                }
        }
    }

    fun loadPopular() {
        viewModelScope.launch {
            _popularState.value = UiState.Loading
            repository.loadPopular()
                .catch { e -> _popularState.value = UiState.Error(e.message ?: "Unknown Error") }
                .collect { items ->
                    _popularState.value = UiState.Success(items)
                }
        }
    }

    fun loadRestaurants() {
        viewModelScope.launch {
            _restaurantState.value = UiState.Loading
            repository.loadRestaurants()
                .catch { e -> _restaurantState.value = UiState.Error(e.message ?: "Unknown Error") }
                .collect { items ->
                    _restaurantState.value = UiState.Success(items)
                }
        }
    }

    fun loadOrders() {
        viewModelScope.launch {
            _ordersState.value = UiState.Loading
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
                val snapshot = repository.firestore.collection("Orders")
                    .whereEqualTo("userId", userId)
                    .get().await()
                val orders = snapshot.documents.mapNotNull { it.toObject(com.example.delivery_app.domain.OrderModel::class.java) }
                    .sortedByDescending { it.timestamp }
                _ordersState.value = UiState.Success(orders)
            } catch (e: Exception) {
                _ordersState.value = UiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    // For ItemsListActivity
    private val _itemsState = MutableStateFlow<UiState<List<ItemsModel>>>(UiState.Loading)
    val itemsState: StateFlow<UiState<List<ItemsModel>>> = _itemsState

    fun loadItems(categoryId: String) {
        viewModelScope.launch {
            _itemsState.value = UiState.Loading
            repository.loadItemCategory(categoryId)
                .catch { e -> _itemsState.value = UiState.Error(e.message ?: "Unknown Error") }
                .collect { items ->
                    _itemsState.value = UiState.Success(items)
                }
        }
    }

    fun uploadDummyData() {
        viewModelScope.launch {
            repository.uploadDummyData()
            loadBanner()
            loadCategory()
            loadPopular()
            loadRestaurants()
            loadOrders()
        }
    }
}

package com.example.delivery_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery_app.domain.ItemsModel
import com.example.delivery_app.domain.RestaurantModel
import com.example.delivery_app.repository.MainRepository
import com.example.delivery_app.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _restaurantState = MutableStateFlow<UiState<RestaurantModel>>(UiState.Loading)
    val restaurantState: StateFlow<UiState<RestaurantModel>> = _restaurantState

    private val _menuState = MutableStateFlow<UiState<List<ItemsModel>>>(UiState.Loading)
    val menuState: StateFlow<UiState<List<ItemsModel>>> = _menuState

    fun loadRestaurantDetails(restaurantId: String) {
        viewModelScope.launch {
            _restaurantState.value = UiState.Loading
            try {
                val snapshot = repository.firestore.collection("Restaurants").document(restaurantId).get().await()
                val restaurant = snapshot.toObject(RestaurantModel::class.java)
                if (restaurant != null) {
                    _restaurantState.value = UiState.Success(restaurant)
                } else {
                    _restaurantState.value = UiState.Error("Restaurant not found")
                }
            } catch (e: Exception) {
                _restaurantState.value = UiState.Error(e.message ?: "Failed to load restaurant")
            }
        }
    }

    fun loadMenu(restaurantId: String) {
        viewModelScope.launch {
            _menuState.value = UiState.Loading
            try {
                val snapshot = repository.firestore.collection("Items")
                    .whereEqualTo("restaurantId", restaurantId)
                    .get().await()
                val items = snapshot.documents.mapNotNull { it.toObject(ItemsModel::class.java) }
                _menuState.value = UiState.Success(items)
            } catch (e: Exception) {
                _menuState.value = UiState.Error(e.message ?: "Failed to load menu")
            }
        }
    }
}

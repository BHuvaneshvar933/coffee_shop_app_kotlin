package com.example.delivery_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery_app.data.local.CartDao
import com.example.delivery_app.data.local.CartEntity
import com.example.delivery_app.domain.ItemsModel
import com.example.delivery_app.repository.MainRepository
import com.example.delivery_app.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MainRepository,
    private val cartDao: CartDao
) : ViewModel() {

    private val _itemState = MutableStateFlow<UiState<ItemsModel>>(UiState.Loading)
    val itemState: StateFlow<UiState<ItemsModel>> = _itemState

    fun loadItem(itemTitle: String) {
        viewModelScope.launch {
            _itemState.value = UiState.Loading
            try {
                // In a real app we'd query by ID, but dummy data uses title for navigation often. Let's query by title.
                val snapshot = repository.firestore.collection("Items")
                    .whereEqualTo("title", itemTitle)
                    .limit(1)
                    .get().await()
                val item = snapshot.documents.firstOrNull()?.toObject(ItemsModel::class.java)
                if (item != null) {
                    _itemState.value = UiState.Success(item)
                } else {
                    _itemState.value = UiState.Error("Item not found")
                }
            } catch (e: Exception) {
                _itemState.value = UiState.Error(e.message ?: "Failed to load item")
            }
        }
    }

    fun addToCart(item: ItemsModel, quantity: Int) {
        viewModelScope.launch {
            val entity = CartEntity(
                id = item.title,
                title = item.title,
                picUrl = item.picUrl.firstOrNull() ?: "",
                price = item.price,
                numberInCart = quantity
            )
            cartDao.insertItem(entity)
        }
    }
}

package com.example.delivery_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery_app.data.local.CartDao
import com.example.delivery_app.data.local.CartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(
    val cartItems: List<CartEntity> = emptyList(),
    val itemTotal: Double = 0.0,
    val tax: Double = 0.0,
    val delivery: Double = 15.0,
    val total: Double = 0.0,
    val taxRate: Double = 0.02
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartDao: CartDao
) : ViewModel() {

    val cartState: StateFlow<CartUiState> = cartDao.getAllCartItems()
        .map { items ->
            val itemTotal = items.sumOf { it.price * it.numberInCart }
            val taxRate = 0.02
            val delivery = 15.0
            val tax = Math.round((itemTotal * taxRate) * 100) / 100.0
            val total = if (items.isEmpty()) 0.0 else Math.round((itemTotal + tax + delivery) * 100) / 100.0

            CartUiState(
                cartItems = items,
                itemTotal = itemTotal,
                tax = tax,
                delivery = if (items.isEmpty()) 0.0 else delivery,
                total = total,
                taxRate = taxRate
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState()
        )

    fun addToCart(item: CartEntity) {
        viewModelScope.launch {
            cartDao.insertItem(item)
        }
    }

    fun incrementQuantity(item: CartEntity) {
        viewModelScope.launch {
            cartDao.insertItem(item.copy(numberInCart = item.numberInCart + 1))
        }
    }

    fun decrementQuantity(item: CartEntity) {
        viewModelScope.launch {
            if (item.numberInCart > 1) {
                cartDao.insertItem(item.copy(numberInCart = item.numberInCart - 1))
            } else {
                cartDao.deleteItem(item)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
        }
    }
}

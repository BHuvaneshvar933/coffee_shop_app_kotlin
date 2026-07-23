package com.example.delivery_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery_app.data.local.CartDao
import com.example.delivery_app.domain.ItemsModel
import com.example.delivery_app.domain.OrderModel
import com.example.delivery_app.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: MainRepository,
    private val cartDao: CartDao
) : ViewModel() {

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    fun placeOrder(address: String, paymentMethod: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                val cartItems = cartDao.getAllCartItemsSynchronous()
                val itemsModelList = cartItems.map {
                    ItemsModel(title = it.title, picUrl = arrayListOf(it.picUrl), price = it.price, numberInCart = it.numberInCart)
                }
                
                val total = itemsModelList.sumOf { it.price * it.numberInCart }
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
                
                val order = OrderModel(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    status = "Order Received",
                    totalPrice = total,
                    items = itemsModelList,
                    timestamp = System.currentTimeMillis()
                )

                repository.firestore.collection("Orders").document(order.id).set(order).await()
                cartDao.clearCart()
                onComplete()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isProcessing.value = false
            }
        }
    }
}

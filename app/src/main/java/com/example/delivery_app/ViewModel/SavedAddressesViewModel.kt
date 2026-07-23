package com.example.delivery_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delivery_app.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SavedAddressesViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _addresses = MutableStateFlow<List<String>>(emptyList())
    val addresses: StateFlow<List<String>> = _addresses

    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    init {
        loadAddresses()
    }

    private fun loadAddresses() {
        if (userId == null) return
        viewModelScope.launch {
            try {
                val document = repository.firestore.collection("Users").document(userId).get().await()
                val loadedAddresses = document.get("addresses") as? List<String> ?: listOf("Home: 123 Main St")
                _addresses.value = loadedAddresses
            } catch (e: Exception) {
                _addresses.value = listOf("Home: 123 Main St")
            }
        }
    }

    fun addAddress(address: String) {
        if (userId == null) return
        viewModelScope.launch {
            val newList = _addresses.value.toMutableList().apply { add(address) }
            _addresses.value = newList
            try {
                val data = hashMapOf("addresses" to newList)
                repository.firestore.collection("Users").document(userId).set(data).await()
            } catch (e: Exception) {
                // handle error
            }
        }
    }
}

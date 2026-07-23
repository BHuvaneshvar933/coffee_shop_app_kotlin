package com.example.delivery_app.repository

import com.example.delivery_app.domain.BannerModel
import com.example.delivery_app.domain.CategoryModel
import com.example.delivery_app.domain.ItemsModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    val firestore: FirebaseFirestore
) {
    fun loadBanner(): Flow<List<BannerModel>> = flow {
        val snapshot = firestore.collection("Banner").get().await()
        val list = snapshot.documents.mapNotNull { it.toObject(BannerModel::class.java) }
        emit(list)
    }

    fun loadCategory(): Flow<List<CategoryModel>> = flow {
        val snapshot = firestore.collection("Category").get().await()
        val list = snapshot.documents.mapNotNull { it.toObject(CategoryModel::class.java) }
        emit(list)
    }

    fun loadPopular(): Flow<List<ItemsModel>> = flow {
        val snapshot = firestore.collection("Popular").get().await()
        val list = snapshot.documents.mapNotNull { it.toObject(ItemsModel::class.java) }
        emit(list)
    }

    fun loadItemCategory(categoryId: String): Flow<List<ItemsModel>> = flow {
        val snapshot = firestore.collection("Items")
            .whereEqualTo("categoryId", categoryId)
            .get()
            .await()
        val list = snapshot.documents.mapNotNull { it.toObject(ItemsModel::class.java) }
        emit(list)
    }

    fun loadRestaurants(): Flow<List<com.example.delivery_app.domain.RestaurantModel>> = flow {
        val snapshot = firestore.collection("Restaurants").get().await()
        val list = snapshot.documents.mapNotNull { it.toObject(com.example.delivery_app.domain.RestaurantModel::class.java) }
        emit(list)
    }

    suspend fun uploadDummyData() {
        // 1. Categories
        val categories = listOf(
            CategoryModel("Pizza", 1, "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=200"),
            CategoryModel("Burger", 2, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=200"),
            CategoryModel("Indian", 3, "https://images.unsplash.com/photo-1585937421612-70a008356fbe?q=80&w=200"),
            CategoryModel("Chinese", 4, "https://images.unsplash.com/photo-1552611052-33e04de081de?q=80&w=200")
        )

        for (c in categories) {
            firestore.collection("Category").document(c.id.toString()).set(c).await()
        }

        // 2. Restaurants
        val restaurants = listOf(
            com.example.delivery_app.domain.RestaurantModel(
                id = "r1", name = "Pizza Palace",
                coverImage = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?q=80&w=800",
                logo = "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=200",
                rating = 4.8, deliveryTime = "25-35 min", deliveryFee = 2.99, distance = "1.2 km", cuisine = "Pizza"
            ),
            com.example.delivery_app.domain.RestaurantModel(
                id = "r2", name = "Burger King",
                coverImage = "https://images.unsplash.com/photo-1550547660-d9450f859349?q=80&w=800",
                logo = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=200",
                rating = 4.5, deliveryTime = "15-25 min", deliveryFee = 1.99, distance = "0.8 km", cuisine = "Burger"
            )
        )

        for (r in restaurants) {
            firestore.collection("Restaurants").document(r.id).set(r).await()
        }

        // 3. Items (Linked to Category & Restaurant)
        val items = listOf(
            ItemsModel("Margherita Pizza", "Classic cheese and tomato", arrayListOf("https://images.unsplash.com/photo-1574071318508-1cdbab80d002?q=80&w=400"), 12.99, 4.5, 0, "", "r1", "1"),
            ItemsModel("Pepperoni Pizza", "Cheesy with pepperoni", arrayListOf("https://images.unsplash.com/photo-1628840042765-356cda07504e?q=80&w=400"), 14.99, 4.8, 0, "", "r1", "1"),
            ItemsModel("Cheeseburger", "Double patty with cheese", arrayListOf("https://images.unsplash.com/photo-1550547660-d9450f859349?q=80&w=400"), 8.99, 4.8, 0, "", "r2", "2")
        )

        for ((index, item) in items.withIndex()) {
            firestore.collection("Popular").document(index.toString()).set(item).await()
            firestore.collection("Items").document(index.toString()).set(item).await()
        }
        
        // 4. Banner
        firestore.collection("Banner").document("1").set(BannerModel("https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=800")).await()
    }
}

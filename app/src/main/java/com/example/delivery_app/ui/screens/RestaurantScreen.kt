package com.example.delivery_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.delivery_app.domain.ItemsModel
import com.example.delivery_app.util.UiState
import com.example.delivery_app.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen(
    restaurantId: String,
    onNavigateBack: () -> Unit,
    onItemClick: (String) -> Unit,
    viewModel: RestaurantViewModel = hiltViewModel()
) {
    val restaurantState by viewModel.restaurantState.collectAsState()
    val menuState by viewModel.menuState.collectAsState()

    LaunchedEffect(restaurantId) {
        viewModel.loadRestaurantDetails(restaurantId)
        viewModel.loadMenu(restaurantId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (restaurantState is UiState.Success) (restaurantState as UiState.Success).data.name else "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (restaurantState is UiState.Loading || menuState is UiState.Loading) {
                item { 
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator() 
                    }
                }
            } else {
                when (val state = restaurantState) {
                    is UiState.Success -> {
                    val restaurant = state.data
                    item {
                        AsyncImage(
                            model = restaurant.coverImage,
                            contentDescription = "Cover",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(restaurant.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Text(" ${restaurant.rating} • ${restaurant.deliveryTime} • ${restaurant.cuisine}", style = MaterialTheme.typography.bodyLarge)
                            }
                            Text("Delivery Fee: $${restaurant.deliveryFee}", style = MaterialTheme.typography.bodyMedium)
                        }
                        HorizontalDivider()
                        Text(
                            "Menu",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                    is UiState.Error -> item { Text(state.message, modifier = Modifier.padding(16.dp)) }
                    else -> {}
                }

                when (val state = menuState) {
                    is UiState.Success -> {
                    items(state.data) { item ->
                        MenuItemCard(item, onItemClick)
                    }
                }
                    is UiState.Error -> item { Text(state.message, modifier = Modifier.padding(16.dp)) }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(item: ItemsModel, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick(item.title) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$${item.price}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            AsyncImage(
                model = item.picUrl.firstOrNull(),
                contentDescription = item.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

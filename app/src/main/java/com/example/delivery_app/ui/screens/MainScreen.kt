package com.example.delivery_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.delivery_app.domain.BannerModel
import com.example.delivery_app.domain.CategoryModel
import com.example.delivery_app.domain.ItemsModel
import com.example.delivery_app.domain.RestaurantModel
import com.example.delivery_app.util.UiState
import com.example.delivery_app.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.delivery_app.ui.components.*
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCart: () -> Unit,
    onNavigateToCategory: (String, String) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToRestaurant: (String) -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToAddresses: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToHelp: () -> Unit = {},
    onNavigateToTracking: (String) -> Unit = {},
    onLogout: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    var currentTab by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Orders") },
                    label = { Text("Orders") },
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentTab) {
                0 -> HomeContent(viewModel, onNavigateToCart, onNavigateToCategory, onNavigateToDetail, onNavigateToRestaurant, onNavigateToSearch = { currentTab = 1 })
                1 -> SearchContent(viewModel, onNavigateToDetail, onNavigateToRestaurant)
                2 -> OrdersContent(viewModel, onNavigateToTracking)
                3 -> ProfileContent(
                    onNavigateToEditProfile = onNavigateToEditProfile,
                    onNavigateToAddresses = onNavigateToAddresses,
                    onNavigateToOrders = { currentTab = 2 },
                    onNavigateToSettings = onNavigateToSettings,
                    onNavigateToHelp = onNavigateToHelp,
                    onLogout = onLogout
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    viewModel: MainViewModel,
    onNavigateToCart: () -> Unit,
    onNavigateToCategory: (String, String) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToRestaurant: (String) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val bannerState = viewModel.bannerState.collectAsState()
    val categoryState = viewModel.categoryState.collectAsState()
    val popularState = viewModel.popularState.collectAsState()
    val restaurantState = viewModel.restaurantState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBanner()
        viewModel.loadCategory()
        viewModel.loadPopular()
        viewModel.loadRestaurants()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Custom Dense Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Delivering to", style = MaterialTheme.typography.bodySmall)
                Text("Home, 123 Main St", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* TODO: Notifications */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
                IconButton(onClick = onNavigateToCart) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            }
        }
        
        if (bannerState.value is UiState.Loading || categoryState.value is UiState.Loading || popularState.value is UiState.Loading || restaurantState.value is UiState.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    // Clickable Search Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                            .clickable { onNavigateToSearch() }
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Search for dishes or restaurants", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                item { Box(modifier = Modifier.padding(horizontal = 16.dp)) { BannerSection(bannerState.value) } }
                item { Box(modifier = Modifier.padding(horizontal = 16.dp)) { CategorySection(categoryState.value, onNavigateToCategory) } }
                item { Box(modifier = Modifier.padding(horizontal = 16.dp)) { RestaurantSection(restaurantState.value, onNavigateToRestaurant) } }
                item { Box(modifier = Modifier.padding(horizontal = 16.dp)) { PopularSection(popularState.value, onNavigateToDetail) } }
            }
        }
    }
}

@Composable
fun RestaurantSection(state: UiState<List<RestaurantModel>>, onRestaurantClick: (String) -> Unit) {
    Column {
        Text("Featured Restaurants", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        when (state) {
            is UiState.Loading -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(3) {
                        ShimmerCardItem(modifier = Modifier.width(220.dp).height(200.dp))
                    }
                }
            }
            is UiState.Success -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(state.data) { restaurant ->
                        Card(
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { onRestaurantClick(restaurant.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column {
                                AsyncImage(
                                    model = restaurant.coverImage,
                                    contentDescription = restaurant.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(restaurant.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                        Text(" ${restaurant.rating} • ${restaurant.deliveryTime}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is UiState.Error -> Text(state.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    viewModel: MainViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToRestaurant: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val itemsState by viewModel.popularState.collectAsState()
    val restaurantsState by viewModel.restaurantState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search for dishes or restaurants...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("Pizza", "Burger", "Healthy", "Fast Delivery", "Top Rated", "Veg")) { filter ->
                FilterChip(
                    selected = false,
                    onClick = { /* TODO: Apply Filter */ query = filter },
                    label = { Text(filter) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        if (query.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Filter Restaurants
                val rState = restaurantsState
                if (rState is UiState.Success) {
                    val filteredRestaurants = rState.data.filter { it.name.contains(query, ignoreCase = true) }
                    if (filteredRestaurants.isNotEmpty()) {
                        item { Text("Restaurants", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                        items(filteredRestaurants) { restaurant ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToRestaurant(restaurant.id) }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = restaurant.logo,
                                    contentDescription = restaurant.name,
                                    modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(restaurant.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text(restaurant.cuisine, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                // Filter Items
                val iState = itemsState
                if (iState is UiState.Success) {
                    val filteredItems = iState.data.filter { it.title.contains(query, ignoreCase = true) }
                    if (filteredItems.isNotEmpty()) {
                        item { Text("Dishes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
                        items(filteredItems) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToDetail(item.title) }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = item.picUrl.firstOrNull(),
                                    contentDescription = item.title,
                                    modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("$${item.price}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Type to start searching", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun OrdersContent(viewModel: MainViewModel, onNavigateToTracking: (String) -> Unit) {
    val ordersState by viewModel.ordersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "My Orders",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        when (val state = ordersState) {
            is UiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No orders yet", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.data) { order ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Order #${order.id.take(8)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Text(order.status, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("${order.items.size} items • $${order.totalPrice}", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedButton(
                                        onClick = { onNavigateToTracking(order.id) },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Track Order")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is UiState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message) }
        }
    }
}

@Composable
fun ProfileContent(
    onNavigateToEditProfile: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onLogout: () -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(user?.displayName ?: "Foodie User", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(user?.email ?: "Unknown Email", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onNavigateToEditProfile, shape = RoundedCornerShape(20.dp)) {
                    Text("Edit Profile")
                }
            }
        }
        
        item { HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp)) }

        item { ProfileOptionRow(icon = Icons.Default.LocationOn, title = "Saved Addresses", subtitle = "Home, Office", onClick = onNavigateToAddresses) }
        item { ProfileOptionRow(icon = Icons.Default.List, title = "Order History", subtitle = "View past orders", onClick = onNavigateToOrders) }
        item { ProfileOptionRow(icon = Icons.Default.Settings, title = "Account Settings", subtitle = "Password, Notifications", onClick = onNavigateToSettings) }
        item { ProfileOptionRow(icon = Icons.Default.Info, title = "Help & Support", subtitle = "FAQ, Contact us", onClick = onNavigateToHelp) }
        
        item { Spacer(modifier = Modifier.height(24.dp)) }
        
        item {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Log Out", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun ProfileOptionRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun BannerSection(state: UiState<List<BannerModel>>) {
    when (state) {
        is UiState.Loading -> ShimmerBannerItem()
        is UiState.Success -> {
            val banners = state.data
            if (banners.isNotEmpty()) {
                AsyncImage(
                    model = banners[0].url,
                    contentDescription = "Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
        is UiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun CategorySection(state: UiState<List<CategoryModel>>, onCategoryClick: (String, String) -> Unit) {
    Column {
        Text("Categories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        when (state) {
            is UiState.Loading -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(4) {
                        ShimmerCategoryItem()
                    }
                }
            }
            is UiState.Success -> {
                val categories = state.data
                val row1 = categories.take(categories.size / 2 + categories.size % 2)
                val row2 = categories.drop(categories.size / 2 + categories.size % 2)

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(row1) { category ->
                            CategoryItem(category, onCategoryClick)
                        }
                    }
                    if (row2.isNotEmpty()) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(row2) { category ->
                                CategoryItem(category, onCategoryClick)
                            }
                        }
                    }
                }
            }
            is UiState.Error -> Text(state.message)
        }
    }
}

@Composable
fun CategoryItem(category: CategoryModel, onCategoryClick: (String, String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable { onCategoryClick(category.id.toString(), category.title) }
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = category.picUrl,
                contentDescription = category.title,
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(category.title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}

@Composable
fun PopularSection(state: UiState<List<ItemsModel>>, onItemClick: (String) -> Unit) {
    Column {
        Text("Popular Items", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        when (state) {
            is UiState.Loading -> {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                        ShimmerCardItem(modifier = Modifier.weight(1f).height(180.dp))
                        ShimmerCardItem(modifier = Modifier.weight(1f).height(180.dp))
                    }
                }
            }
            is UiState.Success -> {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    state.data.chunked(2).forEach { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowItems.forEach { item ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onItemClick(item.title) },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        AsyncImage(
                                            model = item.picUrl.firstOrNull(),
                                            contentDescription = item.title,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(120.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Text("$${item.price}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
            is UiState.Error -> Text(state.message)
        }
    }
}

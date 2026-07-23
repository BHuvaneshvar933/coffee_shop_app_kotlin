package com.example.delivery_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.delivery_app.ui.screens.MainScreen
import com.example.delivery_app.ui.screens.CartScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object Cart : Screen("cart")
    object ItemsList : Screen("items_list/{categoryId}/{categoryTitle}") {
        fun createRoute(categoryId: String, categoryTitle: String) = "items_list/$categoryId/$categoryTitle"
    }
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: String) = "detail/$itemId"
    }
    object Restaurant : Screen("restaurant/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant/$restaurantId"
    }
    object Checkout : Screen("checkout")
    object EditProfile : Screen("edit_profile")
    object SavedAddresses : Screen("saved_addresses")
    object AccountSettings : Screen("account_settings")
    object HelpSupport : Screen("help_support")
    object LiveTracking : Screen("live_tracking/{orderId}") {
        fun createRoute(orderId: String) = "live_tracking/$orderId"
    }
}

@Composable
fun DeliveryNavGraph(navController: NavHostController) {
    val startDest = if (com.google.firebase.auth.FirebaseAuth.getInstance().currentUser != null) {
        Screen.Main.route
    } else {
        Screen.Intro.route
    }

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable(Screen.Intro.route) {
            com.example.delivery_app.ui.screens.IntroScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Login.route) {
            com.example.delivery_app.ui.screens.LoginScreen(
                onNavigateBack = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            com.example.delivery_app.ui.screens.RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                onNavigateToCategory = { id, title ->
                    navController.navigate(Screen.ItemsList.createRoute(id, title))
                },
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                },
                onNavigateToRestaurant = { restaurantId ->
                    navController.navigate(Screen.Restaurant.createRoute(restaurantId))
                },
                onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                onNavigateToAddresses = { navController.navigate(Screen.SavedAddresses.route) },
                onNavigateToSettings = { navController.navigate(Screen.AccountSettings.route) },
                onNavigateToHelp = { navController.navigate(Screen.HelpSupport.route) },
                onNavigateToTracking = { orderId -> navController.navigate(Screen.LiveTracking.createRoute(orderId)) },
                onLogout = {
                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Intro.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(Screen.Cart.route) {
            CartScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) }
            )
        }
        composable(Screen.Checkout.route) {
            com.example.delivery_app.ui.screens.CheckoutScreen(
                onNavigateBack = { navController.popBackStack() },
                onOrderPlaced = { 
                    navController.popBackStack(Screen.Main.route, inclusive = false)
                }
            )
        }
        composable(Screen.ItemsList.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val categoryTitle = backStackEntry.arguments?.getString("categoryTitle") ?: ""
            com.example.delivery_app.ui.screens.ItemsListScreen(
                categoryId = categoryId,
                categoryTitle = categoryTitle,
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Restaurant.route) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            com.example.delivery_app.ui.screens.RestaurantScreen(
                restaurantId = restaurantId,
                onNavigateBack = { navController.popBackStack() },
                onItemClick = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                }
            )
        }
        composable(Screen.Detail.route) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            com.example.delivery_app.ui.screens.DetailScreen(
                itemId = itemId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.EditProfile.route) {
            com.example.delivery_app.ui.screens.EditProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SavedAddresses.route) {
            com.example.delivery_app.ui.screens.SavedAddressesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AccountSettings.route) {
            com.example.delivery_app.ui.screens.AccountSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.HelpSupport.route) {
            com.example.delivery_app.ui.screens.HelpSupportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.LiveTracking.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            com.example.delivery_app.ui.screens.LiveTrackingScreen(
                orderId = orderId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

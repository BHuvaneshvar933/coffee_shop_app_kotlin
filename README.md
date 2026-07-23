# Delivery App - Android

An elegant and functional food delivery Android application built using **Kotlin**, **Jetpack Compose**, and **Clean MVVM Architecture**. Users can browse food items, add them to cart, track their live orders on a map, and manage their profile in a sleek, Zepto-inspired UI.

---

## Features

- **Zepto-Style UI**: Premium purple color scheme, dense category grids, and a clean Material 3 design system.
- **Dynamic Data**: Real-time fetching of categories, restaurants, and popular items from **Firebase Firestore**.
- **Live Order Tracking**: Track active deliveries on a real Google Map using **Google Maps Compose**.
- **Cart Management**: Add/remove items with local persistence using **Room Database**.
- **Authentication**: Secure login/signup flow using **Firebase Authentication**.
- **User Profile**: Manage saved addresses, order history, and account settings.
- **Dark Mode**: Fully supported, beautiful dark mode implementation.
- **Search System**: Unified search tab for filtering dishes and restaurants.

---

## Tech Stack

| Category       | Stack                                        |
|----------------|----------------------------------------------|
| Language       | Kotlin                                       |
| UI Toolkit     | Jetpack Compose + Material 3                 |
| Architecture   | MVVM (Model-View-ViewModel)                  |
| State Mgmt     | StateFlow + Coroutines                       |
| Dependency Inj.| Dagger Hilt                                  |
| Local Storage  | Room Database                                |
| Backend        | Firebase (Firestore + Auth)                  |
| Maps           | Google Maps Compose (`play-services-maps`)   |
| Image Loading  | Coil                                         |

---

## Project Structure
```
app/src/main/java/com/example/delivery_app/
├── activity/
│   └── MainActivity.kt (Single Activity entry point)
├── di/
│   └── AppModule.kt (Hilt Dependency Injection)
├── domain/
│   └── (Models for Banner, Category, Items, Order, Restaurant)
├── local/
│   └── (Room Database entities and DAOs for Cart)
├── navigation/
│   └── NavGraph.kt (Compose Navigation)
├── repository/
│   └── MainRepository.kt (Firestore data fetching)
├── ui/
│   ├── components/ (Reusable UI like Shimmers)
│   ├── screens/ (Compose screens: Home, Detail, Cart, Login, Tracking...)
│   └── theme/ (Zepto-style purple theme definitions)
└── viewmodel/
    └── (ViewModels for state management)
```

## Setup & Run

1. Clone the repo:
   ```bash
   git clone https://github.com/BHuvaneshvar933/coffee_shop_app_kotlin.git
   ```
2. Open in Android Studio.
3. Replace the placeholder Google Maps API Key in `AndroidManifest.xml`.
4. Connect the app to your own Firebase project (ensure Firestore and Auth are enabled).
5. Let it sync Gradle files.
6. Run on an emulator or connected device.

---

## What's Next

- Implement Live Push Notifications.
- Payment Gateway Integration (Stripe/Razorpay).

---

## Contributing
- PRs are welcome! If you have ideas for new features or improvements, feel free to fork the repo and submit a pull request.

## Author
- C. Bhuvaneshvar Reddy

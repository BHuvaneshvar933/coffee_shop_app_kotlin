# 📦 Delivery App - Android

An elegant and functional food delivery Android application built using **Kotlin**, **XML (Jetpack Layouts)**, and **Android Jetpack components**. Users can browse food items, add them to cart, apply discount codes, and proceed to checkout.

---

## 🚀 Features

- 🛒 Add/Remove items from cart
- ➕ Increase or decrease item quantity
- 💸 Apply discount code input field
- 📦 View subtotal, tax, delivery fee, and total
- 🖼️ Product image loading using **Glide**
- 📦 Cart management using custom `TinyDB` helper (local storage)

---

## 🧰 Built With

- Kotlin
- Android SDK (min SDK 24)
- RecyclerView
- SQLite (custom handler)
- Material Design Components (Material 3)
- AndroidX libraries

---


## 🖼️ Screenshots

### Home Screen
<img src="app/sampledata/s4.jpeg" alt="Home Screen" width="300"/>

### Product Details
<img src="app/sampledata/s3.jpeg" alt="Edit Task" width="300"/>

### Product List
<img src="app/sampledata/s1.jpeg" alt="Edit Task" width="300"/>

### Cart Page
<img src="app/sampledata/s2.jpeg" alt="Edit Task" width="300"/>

---

## 📂 Project Structure
```
app/
├── manifests/
│ └── AndroidManifest.xml
├── java/
│ └── com.example.delivery_app/
│ ├── Activity/
│ │ ├── CartActivity.kt
│ │ ├── DetailActivity.kt
│ │ ├── ItemsListActivity.kt
│ │ ├── MainActivity.kt
│ │ └── SplashActivity.kt
│ ├── Adapter/
│ │ ├── CartAdapter.kt
│ │ ├── CategoryAdapter.kt
│ │ ├── ItemListCategoryAdapter.kt
│ │ └── PopularAdapter.kt
│ ├── Domain/
│ │ ├── BannerModel.kt
│ │ ├── CategoryModel.kt
│ │ └── ItemsModel.kt
│ ├── Helper/
│ │ ├── ChangeNumberItemsListener.kt
│ │ ├── ManagmentCart.kt
│ │ └── TinyDB.kt
│ ├── Repository/
│ │ └── MainRepository.kt
│ └── ViewModel/
│ └── MainViewModel.kt
├── res/
│ ├── drawable/
│ │ ├── Icons: back, bell_icon, close, star, plus, profile, search_icon, settings, splash_pic
│ │ ├── Backgrounds: brown_gradient_bg.xml, brown_storke_bg.xml, cream_bg.xml, white_bg.xml, etc.
│ ├── layout/
│ │ ├── activity_cart.xml
│ │ ├── activity_detail.xml
│ │ ├── activity_items_list.xml
│ │ ├── activity_main.xml
│ │ ├── activity_splash.xml
│ │ ├── viewholder_cart.xml
│ │ ├── viewholder_category.xml
│ │ ├── viewholder_item_list.xml
│ │ └── viewholder_popular.xml
│ ├── mipmap/
│ │ ├── ic_launcher/
│ │ └── ic_launcher_round/
│ ├── values/
│ │ ├── colors.xml
│ │ ├── strings.xml
│ │ └── themes.xml
│ └── xml/
│ ├── backup_rules.xml
│ └── data_extraction_rules.xml
```

## 🛠️ Tech Stack

- **Language:** Kotlin  
- **UI:** XML + ConstraintLayout + RecyclerView  
- **Image Loading:** Glide  
- **Storage:** TinyDB (SharedPreferences wrapper)  
- **Architecture:** MVVM-like with Helper & Adapter pattern

---

## 🧪 Setup & Run

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/DeliveryApp.git
2. Open in Android Studio

3. Let it sync Gradle files.

4. Run on emulator or connected device.

## 🧠 What's Next

- Complete the details in Explore, Wishlist, orders, and Profile
- Add Google login

---

## 🤝 Contributing
- PRs are welcome! If you have ideas for new features or improvements, feel free to fork the repo and submit a pull request.

## 📜 License
- This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author
- C. Bhuvaneshvar Reddy


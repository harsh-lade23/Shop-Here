# Shop Here 🛍️

A modern, robust Android e-commerce application designed to provide a seamless clothing store experience. Built entirely with Kotlin and Jetpack Compose, this project demonstrates industry-standard development practices, including Clean Architecture and Dependency Injection.

## ✨ Features

* **Authentication:** Secure email-based user authentication.
* **Product Discovery:** Intuitive product browsing experience.
* **Shopping Cart & Wishlist:** Fully functional cart for managing purchases and a wishlist for saving favorite items.
* **Profile Management:** Dedicated user profile section.
* **Order Management:** Simulated order placement flow.

## 🛠 Tech Stack

* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose
* **Architecture:** Clean Architecture & MVVM
* **Dependency Injection:** Dagger Hilt
* **Backend as a Service (BaaS):** Firebase Firestore (Database) & Firebase Storage (Media)

## 🏗 Architecture Details

The application was purposefully refactored from a monolithic centralized ViewModel approach to a scalable, domain-driven structure. Key feature domains include:
* `Auth`
* `Product`
* `Cart`
* `Wishlist`

This modular approach ensures high maintainability, separation of concerns, and readiness for future scaling.

## 🚀 Setup & Installation

1. Clone this repository.
2. Open the project in Android Studio.
3. Connect your Firebase project:
   * Create a Firebase project and enable Firestore, Storage, and Email Authentication.
   * Download the `google-services.json` file and place it in the `app/` directory.
4. Build and run on an Android emulator or physical device.

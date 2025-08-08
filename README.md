
# WatchMyTank - Aquarium Maintenance Assistant
<!-->
<p align="center">
  <img src="httpspre_filled_prompts/WatchMyTank/dashboard.png" width="200" alt="WatchMyTank Dashboard">
  <img src="httpspre_filled_prompts/WatchMyTank/history.png" width="200" alt="Parameter History">
  <img src="httpspre_filled_prompts/WatchMyTank/chart.png" width="200" alt="Parameter Chart">
</p>
<-->
**An Android application designed for aquarium hobbyists to simplify maintenance schedules and track critical water parameters. This app showcases a modern, multi-module MVI architecture, Jetpack Compose, and a product-focused mindset.**

---

## 📖 Table of Contents

-   [The "Why" - Problem & Solution](#-the-why---problem--solution)
-   [✨ Features](#-features)
-   [🎥 App in Action (Demo)](#-app-in-action-demo)
-   [🏛️ Technical Architecture](#️-technical-architecture)
-   [🛠️ Tech Stack & Key Libraries](#️-tech-stack--key-libraries)
-   [🚀 Setup & Build Instructions](#-setup--build-instructions)
-   [🌟 Standout Feature Concept: Test Strip Scanner](#-standout-feature-concept-test-strip-scanner)

---

## 🤔 The "Why" - Problem & Solution

### The Problem

For many beginner and intermediate aquarium hobbyists, maintaining a stable and healthy aquatic environment is a constant challenge. Remembering varied feeding and maintenance schedules is difficult, and manually logging water parameters on paper or in spreadsheets is tedious, error-prone, and makes it nearly impossible to spot gradual, but critical, trends in water quality. This can lead to stress for the hobbyist and potential health issues for the aquarium's inhabitants.

### The Solution

**WatchMyTank** provides a simple, intuitive, and reliable solution. It acts as a digital logbook and a personal assistant for your aquarium.

-   **Reliable Reminders:** Never miss a feeding or a water change again with a flexible, customizable reminder system.
-   **Effortless Logging:** Quickly log key water parameters like pH, Temperature, Ammonia, Nitrite, and Nitrate.
-   **Actionable Insights:** Visualize parameter history through interactive charts, allowing you to easily spot trends and ensure stability in your tank's ecosystem.
-   **Offline First:** Log your readings instantly, even with poor connectivity near your tank. The app will sync your data to the cloud automatically when you're back online.

---

## ✨ Features

### Current MVP Features

*   **Customizable Reminders:**
    *   Create reminders for any task (e.g., "Morning Feed," "Weekly Water Change").
    *   Set flexible schedules: Daily, Every N Days, or via CRON expressions for full control.
    *   Receive local notifications, so you get alerts even if the app is in the background.
*   **Parameter Logging & History:**
    *   Log essential water parameters with timestamps.
    *   View a clean, scrollable history for each parameter.
*   **Data Visualization:**
    *   View historical data for each parameter on a clean, interactive line chart.
    *   Easily switch between different parameter charts.
*   **Secure Cloud Sync:**
    *   Powered by **Firebase Anonymous Auth**, all data is securely tied to your unique user ID.
    *   **Firestore Security Rules** ensure your data is private and can only be accessed by you.
*   **Offline Support:**
    *   Thanks to a **Room** database cache and **WorkManager**, parameter logs are saved locally first and synced to the cloud in the background, ensuring no data is ever lost.

### Future Roadmap & Enhancements

*   **Intelligent Alerts:** Set ideal parameter ranges and receive a notification if a logged value is outside the safe zone.
*   **Multi-Tank Support:** Manage several aquariums from a single app.
*   **Photo-Backed Logs:** Attach a photo of your test strip or tank to each parameter entry for a visual record.
*   **Species Database:** An in-app guide to fish and plant species, including their ideal water parameters.
*   **Test Strip Scanner:** Use the device's camera to automatically read and input values from a chemical test strip.

---
<!-->
## 🎥 App in Action (Demo)

*(Here, you should embed GIFs or a short video. Create these using a screen recorder. They are far more effective than static images.)*

| Dashboard & Reminders                                                                 | Parameter Logging & Charts                                                                 |
| ------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| *[**INSERT GIF HERE** showing the main dashboard, tapping on reminders, and the list]* | *[**INSERT GIF HERE** showing logging a new parameter, viewing history, and the chart]* |

<-->

## 🏛️ Technical Architecture

This project follows a modern, scalable, and testable **Multi-Module MVI (Model-View-Intent)** architecture. This approach enforces a unidirectional data flow, ensures a clean separation of concerns, and promotes a highly decoupled codebase.

### Architecture Diagram

```
+-----------------------------------------------------------------+
|                              :app                               |
| (Application Class, DI Graph, Navigation, Firebase Integration) |
+-----------------------------------------------------------------+
       |                  |                  |
       |                  |                  |
       v                  v                  v
+-----------------+ +--------------------+ +----------------------+
| :feature-       | | :feature-          | | :feature-auth        |
|   reminders     | |   parameters       | | (Future - Login/UI)  |
| (UI & ViewModel)| | (UI & ViewModel)   | |                      |
+-----------------+ +--------------------+ +----------------------+
       |                  |
       +------------------+------------------+
                          |
                          v
+-----------------------------------------------------------------+
|                             :domain                             |
|              (UseCases, Models, Repository Interfaces)          |
+-----------------------------------------------------------------+
                          |
                          v
+-----------------------------------------------------------------+
|                              :data                              |
|   (Repository Impls, Room DB, WorkManager, Firestore Client)    |
+-----------------------------------------------------------------+
                          |
                          v
+-----------------------------------------------------------------+
|                              :core                              |
|                  (Shared UI, Theme, Utils, DI)                  |
+-----------------------------------------------------------------+
```

This structure adheres to **SOLID** principles and keeps the logic organized, making the app easier to maintain and scale.

---

## 🛠️ Tech Stack & Key Libraries

-   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for the entire UI layer, leveraging Material 3 components.
-   **Architecture:** MVI with Unidirectional Data Flow.
-   **Async:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html) for all asynchronous operations.
-   **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for managing dependencies across the app.
-   **Local Persistence:** [Room](https://developer.android.com/training/data-storage/room) for caching parameter logs offline.
-   **Background Processing:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) for scheduling reminders and reliable background data sync.
-   **Backend & Cloud:**
    -   **Authentication:** [Firebase Authentication](https://firebase.google.com/docs/auth) (Anonymous Auth).
    -   **Database:** [Cloud Firestore](https://firebase.google.com/docs/firestore) for storing user data securely.
    -   **Stability:** [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics) for crash reporting.
-   **Charting:** [vico](https://github.com/patrykandpatrick/vico).
-   **Navigation:** [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation).

---
<!-->
## 🚀 Setup & Build Instructions

1.  **Clone the Repository**
    ```sh
    git clone https://github.com/seyfettiny/WatchMyTank.git
    ```
2.  **Firebase Setup**
    -   Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
    -   Register your app with the package name `com.syfttny.watchmytank`.
    -   Download the `google-services.json` file and place it in the `:app` module's root directory (`app/`).
    -   In the Firebase Console, enable **Authentication** (with the "Anonymous" sign-in provider) and **Firestore**.
3.  **Open in Android Studio**
    -   Open the project in a recent version of Android Studio.
    -   Let Gradle sync the dependencies.
4.  **Build and Run**
    -   Build and run the app on an emulator or a physical device.

<-->

## 🌟 Standout Feature Concept: Test Strip Scanner

As a forward-looking feature that demonstrates advanced technical capabilities, the app is designed to eventually include a **Test Strip Scanner**.

-   **User Value:** Provides instant, accurate readings by analyzing the colors on a standard aquarium test strip, which is a huge accessibility win for users who are color-blind and a major convenience for all.
-   **Technical Skills Showcased:**
    -   **CameraX Integration:** Building a robust camera preview and image capture pipeline.
    -   **Computer Vision (OpenCV):** For ROI (Region of Interest) detection, perspective correction, and color space analysis.
    -   **On-Device Machine Learning (TensorFlow Lite):** Training a lightweight regression model to map sampled colors to numeric parameter values, making it robust against varied lighting conditions.

This feature highlights a deep understanding of the user's problems and the ability to leverage advanced Android capabilities to create an elegant, high-value solution.
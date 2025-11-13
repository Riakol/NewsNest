# NewsNest

NewsNest is a modern Android news application built entirely with Kotlin and Jetpack Compose. It demonstrates a clean, scalable architecture and leverages the latest Android development technologies to fetch and display news from The Guardian API.

## Features

*   **Dynamic News Feed**: Browse the latest articles in a clean, scrollable list.
*   **Article Details**: Click on any article to view its full content, including images and styled text, rendered in a native WebView.
*   **Search Functionality**: Easily search for news articles on any topic.
*   **Topic Filtering**: Select from a predefined list of categories to filter the news feed.
*   **Clean & Modern UI**: A user-friendly interface built with Jetpack Compose and Material Design 3.
*   **Light/Dark Theme**: The app respects the system's theme and provides a consistent look and feel.

## Architecture

This project follows the principles of **Clean Architecture**, separating concerns into three distinct layers. This separation makes the codebase more robust, scalable, testable, and easier to maintain.

### Architecture Layers

*   **`domain`**: This is the core layer of the application. It contains the business logic, models, and repository interfaces. It is a pure Kotlin module with no dependencies on the Android framework, making the business logic platform-independent.
*   **`data`**: This layer is responsible for providing data to the domain layer. It contains implementations of the repository interfaces defined in the domain layer. It handles all data sources, such as the remote Guardian API (using Retrofit) and maps the network data models (DTOs) to the domain models.
*   **`presentation`**: This layer is responsible for the UI. It is built entirely with Jetpack Compose and follows the MVVM (Model-View-ViewModel) pattern. ViewModels interact with the domain layer's use cases to fetch data and manage the UI state, which is then observed and rendered by the Composable UI components.

```
+---------------------+      +-------------------+      +------------------------+
|    Presentation     |      |       Domain      |      |          Data          |
| (Jetpack Compose,   |      | (Use Cases,       |      | (Retrofit, Room,       |
|  ViewModel, State)  |      |  Models,          |      |  Repository Impl)      |
+---------------------+      |  Repository       |      +------------------------+
|        ^            |      |  Interfaces)      |      |           ^            |
|        |            |      +-------------------+      |           |            |
|        +---------------------------->|            |<----------+            |
+--------------------------------------------------------------------------+

```

## Tech Stack & Libraries

This project leverages a variety of modern libraries and tools in the Android ecosystem.

*   **Kotlin**: The official programming language for Android development.
*   **Jetpack Compose**: A modern toolkit for building native Android UI.
*   **Clean Architecture**: A well-established architectural pattern for building robust and maintainable applications.
*   **MVVM (Model-View-ViewModel)**: A UI architecture pattern that separates the UI from the business logic.
*   **Hilt**: A dependency injection library for Android that reduces boilerplate.
*   **Coroutines & Flow**: Used for managing background threads and handling asynchronous data streams.
*   **Retrofit**: A type-safe HTTP client for making network requests to The Guardian API.
*   **Navigation Compose**: For navigating between composable screens.
*   **Coil**: An image loading library for Android backed by Kotlin Coroutines.
*   **Material Design 3**: The latest version of Google's design system.

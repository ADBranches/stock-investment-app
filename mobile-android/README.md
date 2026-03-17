# Mobile Android App

This module contains the Android client for the Stock Investment App.

## Tech Stack
- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Hilt
- Retrofit
- Room
- Coroutines
- StateFlow

## Architecture
The app follows a layered structure:

- `features/` for UI screens and screen-specific logic
- `navigation/` for app routing
- `core/` for shared constants, utilities, extensions, UI components, and theme
- `data/` for remote/local data access
- `domain/` for core models
- `di/` for dependency injection

## Current Scope of Phase 2
- Compose foundation
- Navigation skeleton
- Theme and shared components
- Session manager
- Retrofit provider
- Basic repository structure
- Splash and onboarding shell

## Design Direction
The UI should feel premium, modern, bold, polished, and investor-trustworthy — similar in visual confidence to strong React + Tailwind startup products, while remaining Android-native.

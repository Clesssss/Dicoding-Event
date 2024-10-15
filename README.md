# Dicoding-Event

### Android Project for Dicoding Android Fundamental Module Submission  
  
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Status](https://img.shields.io/badge/Status-In%20Progress-yellow)

## Table of Contents
- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Screenshots](#screenshots)

## About the Project
This Android application demonstrates the use of a multi-fragment activity architecture with RecyclerView to display data dynamically fetched from an API using Retrofit. The app follows MVVM (Model-View-ViewModel) architecture, utilizing LiveData for efficient and responsive UI updates, and integrates a simple search feature to filter the displayed data in real-time.

## Features
- Multi-Fragment Navigation
- RecyclerView with LiveData
- Retrofit for API Requests
- MVVM Architecture
- Search Functionality
- Data Caching

## Tech Stack
- **Android**: Kotlin, Jetpack Compose, LiveData, RecyclerView, Retrofit for API integration
- **Architecture**: MVVM (Model-View-ViewModel)
- **Tools**: Android Studio, Gradle

## Installation
1. Clone the repository
   ```bash
   git clone https://github.com/Clesssss/Dicoding-Event
   ```
2. Open the project in Android Studio
3. Sync the project with Gradle files and install dependencies
4. Run the app on an Android device or emulator

## Usage
- Navigate to the "Upcoming" or "Finished" section to view the subsequent event.
- Try searching to find an event by name.
- Click one of the event in the RecyclerView to view the event details.

## Screenshots
### Home Fragment
<img src="https://raw.githubusercontent.com/Clesssss/Dicoding-Event/main/screenshots/home_fragment.png" alt="Home Fragment" width="400" />  

### Upcoming Fragment
<img src="https://raw.githubusercontent.com/Clesssss/Dicoding-Event/main/screenshots/upcoming_fragment.png" alt="Upcoming Fragment" width="400" />

### Finished Fragment
<img src="https://raw.githubusercontent.com/Clesssss/Dicoding-Event/main/screenshots/finished_fragment.png" alt="Finished Fragment" width="400" />

### Event Detail
<img src="https://raw.githubusercontent.com/Clesssss/Dicoding-Event/main/screenshots/event_detail.png" alt="Event Detail" width="400" />


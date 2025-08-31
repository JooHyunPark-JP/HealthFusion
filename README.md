
# HealthFusion

[**Download APK (Latest)**](../../releases/latest)

HealthFusion is an Android app that tracks and manages workouts and goals with an offline-first architecture.  
Data is stored locally (in the Room) and synchronized to Firebase Firestore when connectivity is available.

## Screenshots
<img src="./image/image1.png" alt="Example Image" width="200"/> <img src="./image/image2.png" alt="Example Image" width="200"/> <img src="./image/image5.png" alt="Example Image" width="200"/> <img src="./image/image3.png" alt="Example Image" width="200"/> <img src="./image/image6.png" alt="Example Image" width="200"/>

## Features

- **Workout Tracking**:
  - **Aerobic Workouts**: Log exercises like running, cycling, and walking with time, distance, and calories burned.
  - **Anaerobic Workouts**: Track strength-based exercises like push-ups and squats, including sets, repetitions, and weights.

- **Goal**:
  - Custom daily/weekly goals with automatic progress updates based on logged workouts
    
- **Visualization**:
  - Visualize workout data using a line chart.
  - A calendar view allows users to check each workout’s date easily.

- **Auth & Sync**
  - Sign in/up with Firebase Authentication
  - Offline write → automatic Room ↔ Firestore sync on reconnect
    
## Tech Stack

- **Jetpack Compose**: Modern, reactive UI.
- **MVVM Architecture**: Clean separation of concerns.
- **Hilt**: Dependency injection.
- **Room Database**: Offline local data storage.
- **Firebase Firestore**: Cloud-based data synchronization.
- **Firebase Authentication**: User authentication.
- **Coroutines + Flow**: Asynchronous programming.
- **Build:** Gradle (Kotlin DSL), KSP

## License
Distributed under the MIT License. See LICENSE for more information.
   

# Job Test Application

This is a sample Android application for job finding, built using Kotlin and Java. The project uses the MVVM architecture pattern and integrates with a backend service for user authentication and job management.

## Features

- User Registration
- Job Search
- Save Jobs
- Apply to Jobs
- Notifications
- Error Handling
- MVVM Architecture

## Technologies Used

- Kotlin
- Java
- Android Studio
- Gradle
- Coroutines
- LiveData
- ViewModel
- Jetpack Compose

## Project Structure

- `app/src/main/java/com/example/job_test/ui/viewmodel/RegisterViewModel.kt`: Contains the `RegisterViewModel` class for handling user registration logic.
- `app/src/main/java/com/example/job_test/ui/viewmodel/JobViewModel.kt`: Contains the `JobViewModel` class for handling job-related logic.
- `app/src/main/java/com/example/job_test/ui/viewmodel/ProfileViewModel.kt`: Contains the `ProfileViewModel` class for handling user profile logic.
- `app/src/main/java/com/example/job_test/ui/screens/HomeScreen.kt`: Contains the `HomeScreen` composable function for displaying the home screen.
- `app/src/main/java/com/example/job_test/ui/screens/ProfileScreen.kt`: Contains the `ProfileScreen` composable function for displaying the user profile screen.
- `app/src/main/res/drawable/ic_launcher_background.xml`: Defines the background for the app launcher icon.
- `.gitignore`: Specifies files and directories to be ignored by Git.

## Getting Started

### Prerequisites

- Android Studio
- Gradle

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/NikhilBhatt-15/job_test.git
    ```
2. Open the project in Android Studio.
3. Build the project to download dependencies.

### Running the App

1. Connect an Android device or start an emulator.
2. Click on the "Run" button in Android Studio.

## Usage

- Open the app and navigate to the registration screen.
- Enter your email, password, first name, and last name.
- Click on the "Register" button to create a new account.
- Search for jobs, save jobs, and apply to jobs.
- Receive notifications for job updates.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

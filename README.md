# F1+X Desktop

A lightweight JavaFX desktop shell for the F1+X web application. It provides a custom application window, navigation controls, fullscreen support, and an offline fallback page.

## Features

- JavaFX desktop application with a custom title bar
- Embedded web interface using `WebView`
- Back and forward browser navigation
- Minimize, maximize, restore, and close controls
- Automatic connection checks every 10 seconds
- Offline fallback page when the remote server is unavailable
- Custom fonts, icons, and styling

## Requirements

- Windows
- JDK 24 or newer
- Maven Wrapper (included)
- The F1+X web application running at `http://localhost:8080`

## Run the App

From the project root, run:

```powershell
.\mvnw.cmd clean javafx:run
```

The application looks for the web app at:

```text
http://localhost:8080
```

You can change this address in `src/main/java/com/app/f1xdesktop/utils/Constants.java`.

## Offline Behavior

If `http://localhost:8080` cannot be reached, the app displays `fallback.html`. It continues checking the server in the background and automatically loads the remote app once the connection becomes available.

## Project Structure

```text
src/main/java/
├── com/app/f1xdesktop/
│   ├── F1xApplication.java       # JavaFX application entry point
│   ├── controller/               # Window and navigation controller
│   ├── handlers/                 # Connection and browser history handling
│   └── utils/                    # Constants and UI utilities
└── module-info.java

src/main/resources/
├── com/app/f1xdesktop/           # FXML layout and fallback HTML
└── static/                       # CSS, fonts, icons, and images
```

## Build

Compile and package the project with Maven:

```powershell
.\mvnw.cmd clean package
```

## Technology

- Java 24
- JavaFX 24
- Maven
- FXML
- CSS

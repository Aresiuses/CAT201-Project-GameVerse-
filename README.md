# GameVerse Digital Game Store - CAT201 Project

### Project Name: GameVerse
### Objective:  Develop a cross-platform digital game store e-commerce application using a pure Java backend (Servlets, OOP)and a web frontend (HTML/CSS/JS).
### Current Status: Core data model, persistence layer, main store view, and IGDB API integration skeleton are complete.
### 1. Project Setup & Prerequisites
This project requires  a specific environment to run the Servlets.

#### 1.1 Development Environment
* Java: JDK 8 or newer
* IDE: IntelliJ IDEA
* Web Server: Apache Tomcat 9.0.x 

#### 1.2 Dependencies
The following JAR files **MUST** be present in the GameVerse/lib folder and added to the project's module dependencies (Classpath) in IntelliJ:
1. servlet-api.jar: (From your Tomcat installation's /lib folder) - Required for compiling Servlets.
2. gson-x.x.x.jar: (Recommended version 2.10.1 or later) - Required for JSON serialization/deserialization in DatabaseHandler.java.

#### 1.3 External API Config
The GameDataAPIService.java requires valid credentials to authenticate with the IGDB API via Twitch.

**ACTION REQUIRED** (Backend Specialist):

Obtain your Twitch Client ID and Secret.

Update the static variables in: `src/service/GameDataAPIService.java`

```
private static final String TWITCH_CLIENT_ID = "YOUR_TWITCH_CLIENT_ID";
private static final String TWITCH_CLIENT_SECRET = "YOUR_TWITCH_CLIENT_SECRET";
```

### 2. Deployment Guide (IntelliJ IDEA)

1. Open Project in IntelliJ.

2. Verify Configuration: Go to Run > Edit Configurations... and verify the "GameVerse Tomcat" configuration exists.

3. Check Context Path: Ensure the Application Context on the Deployment tab is set to /.

4. Run: Click the green 'Run' button (or Shift+F10).

5. Access: The application will open in the browser at http://localhost:8080/.

### 3. Testing the IGDB API Call

The IGDB API call is currently set up to run automatically when the server starts up (inside the DatabaseHandler constructor).

Verification Steps:

1. Run Tomcat.

2. Check the IntelliJ console output immediately.

3. Expected Success Output: Look for the raw JSON response log:

``` 
--- IGDB RAW JSON RESPONSE START ---
... (JSON data here) ...
--- IGDB RAW JSON RESPONSE END --- 

```


If you see this, your API credentials and network setup are correct. If you see errors (e.g., Response Code: 401 or 400), the credentials are wrong.

### 4. Data Model Focus (Digital Goods)

The Game model has been updated to reflect digital sales:

The stockCount field and related logic have been removed (please remove the stock from mock data).

A new field, downloadSizeGB, and a business method, startDownload(), were added to simulate the post-purchase process.

Git Usage Reminder:
Please commit your changes frequently, use meaningful commit messages, and coordinate branching to avoid merge conflicts!
# GameVerse Digital Game Store - CAT201 Project

### Project Name: GameVerse
### Objective:  Develop a cross-platform digital game store e-commerce application using a pure Java backend (Servlets, OOP)and a web frontend (HTML/CSS/JS).
### Current Status: Project are complete and functionable.
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

#### 2.1. Library Config
1. Open the project in IntelliJ.

2. Go to File > Project Structure > Modules.

3. Select the Dependencies tab.

4. Click the + icon > JARs or Directories.

5. Select the gson-2.10.1.jar and servlet-api.jar from your /lib folder.

6. Click Apply and OK to save the configuration

#### 2.2. Tomcat Server Setup
If using IntelliJ IDEA Ultimate:

1. Click Add Configuration (top right).

2. Click + > Tomcat Server > Local.

3. Point "Application Server" to your Tomcat 9 folder.

4. Go to the Deployment tab.

5. Click + > Artifact > GameVerse:war exploded.

6. Set "Application context" to /.

7. Add JSON file in the repository to /bin folder in Tomcat folder to import data used during testing stage.

If using IntelliJ IDEA Community (Smart Tomcat Plugin):

1. Install the Smart Tomcat plugin from the Marketplace.

2. Click Add Configuration > Smart Tomcat.

3. Tomcat Server: Select your Tomcat folder.

4. Deployment Directory: Select your web folder.

5. Context Path: Set to project folder to use the JSON file provided or to / to start from zero

### 3. Project Testing
1. Run the Project: Click the green Play button in IntelliJ.

2. Access the Store: Open http://localhost:8080/ in your browser.

3. User Flow:

    * Register a new account via the Signup page or login using user credentials via Login page (check users.json for credentials).

    * Select a platform (PC/PS5/etc.) and add a game to the Cart.

    * Complete the Checkout to see the game appear in your Library.

    * For PC games, test the Install/Play/Uninstall simulation.
  
    * Check the setting menu (Profile page) to update username or change password and to view purchase history

4. Admin Flow:

    * Log in with an admin account (check users.json for credentials).

    * Access the Admin Dashboard via the menu.

    * Enter a game title (e.g., "The Witcher 3") and click Add Game. The system will automatically fetch the cover art and metadata.

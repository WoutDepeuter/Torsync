

# Torsync

**javashortburst project**

## Description
Making torrenting easy for yts and automatically downloading them using qbittorent

## Installation
Based on the retrieved information, here are the installation instructions for the Torsync project using qBittorrent Web App:

### Installation Instructions
1. **Setup qBittorrent Web App:**
   - Install qBittorrent on your machine.
   - Enable the Web UI in the qBittorrent settings.
   - Set the credentials for the Web UI (default are `admin` and `adminadmin`).
2. **Clone the Repository:**
   ```sh
   git clone https://github.com/WoutDepeuter/Torsync.git
   cd Torsync
   ```
3. **Configure the Application:**
   - Ensure the `QbittorrentDownloader.java` has the correct qBittorrent URL, username, and password:
     ```java
     private static final String QB_URL = "http://localhost:8081/api/v2";
     private static final String QB_USERNAME = "admin";
     private static final String QB_PASSWORD = "adminadmin";
     ```

4. **Run the Application:**
   - Build and run the application using your preferred method (e.g., using an IDE or command line).
5. **Using the Application:**
   - Access the web interface and use the search functionality to find movies.
   - Submit a magnet link to download the content via the qBittorrent Web App.
For more details, you can view the relevant file [here](https://github.com/WoutDepeuter/Torsync/blob/8ed791d80dff9295b9af0aba107bdc143d97fa7f/src/main/java/org/ehb/wout/torsync/QbittorrentDownloader.java#L5-L75).



## Usage
Browsing torrents and downloading them

## Contribution

## Credits
- [WoutDepeuter](https://github.com/WoutDepeuter) - Main contributor



Would you like to add or modify any sections?

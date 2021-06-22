# Overview
This assignment displays the trends in cryptocurrency pricing using JavaFX. It uses coinbase.com's API to fetch prices of each cryptocurrency in USD and CAD. The communication to the API is done purely using sockets. 

# Requirements
- OpenJDK v11+
- Maven v3.6.3

# Usage
```
git clone https://github.com/karanj798/CryptoTracker.git
cd server 
mvn javafx:compile
mvn javafx:run

cd client
mvn javafx:compile
mvn javafx:run
```

# Note
This application will not run if its executed from wsl, it must be executed from native terminal support of the OS.
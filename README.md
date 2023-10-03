# weather_app
Simple weather app utilizing the openweathermap.org weather API and the Android GMS location provider. 

Click on the floating action button to search for a city. A city or a city,state pair can be entered which will return a city with weather data if the city exists. Only US cities can be looked up at this time. The query has a ",USA" appended to it. This limitation can be removed later. Imperial units are shown for temperatures and wind speeds. A toggle will be added later to switch between metric and imperial units. On first startup, the app will try to get the current location and show the weather for that location and save that location. The last searched location will overwrite any previously saved location and will use this location to get the weather on startup. Presently the app will only save a single location. This will be updated later to support a database (ROOM) to save the locations and weather data. 

Clone the repo, checkout master, add your own map API key in the com.ttyl.weatherapp.WeatherBusiness WEATHER_API_KEY constant. Connect a phone or emulator and run it. 




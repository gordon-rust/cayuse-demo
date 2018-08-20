## Cayuse Demo Application
This is a simple project that takes a zip code as input and queries three different rest api's. The data is then aggregated into a response object of the pertinent information along with a friendly message.

### Application
The application is a Spring Boot application and can be ran simply in IntelliJ by cloning and utilizing the default run configuration. The application can then be reached via Postman at the following URL.
* localhost:8080/weather/location?zipCode=79756

### Usage
The application is utilized by entering a valid US zip code. If a valid zip code is not entered a custom error response is generated.

### Expected Results
If everything runs properly the expected results for a zip code of 78666 is:
```json
{
    "cityName": "Austin",
    "timezone": "Central Daylight Time",
    "temperature": 80.91,
    "elevation": 178.63,
    "message": "The temperature in Austin currently is 80.91 degrees fahrenheit. Austin is in the Central Daylight Time Zone and has an elevation of 178.63 meters above sea level."
}
```
### Libraries
The application utilizes Lombok which requires the plugin to be added to IntelliJ. This is used to reduce the amount of boilerplate code neccessary.

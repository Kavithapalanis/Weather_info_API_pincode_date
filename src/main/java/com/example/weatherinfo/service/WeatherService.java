package com.example.weatherinfo.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.weatherinfo.modelentity.PincodeLocation;
import com.example.weatherinfo.modelentity.WeatherInfo;
import com.example.weatherinfo.repository.WeatherRepository;
import com.example.weatherinfo.repository.PincodeLocationRepository;//**********

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WeatherService {
	
	@Value("${openweather.api.key}")
	private String apiKey;
	
    private final WeatherRepository weatherRepository;
    private final PincodeLocationRepository pincodeLocationRepository;
    
    private final RestTemplate restTemplate;
    
    public WeatherService(WeatherRepository weatherRepository, PincodeLocationRepository pincodeLocationRepository, RestTemplate restTemplate) {
        this.weatherRepository = weatherRepository;
        this.pincodeLocationRepository = pincodeLocationRepository;
        this.restTemplate = restTemplate;
    }
    
    public List<Map<String, Object>> getWeatherInfo(String pincode, String date) {
        // to Fetch weather info from the repository
        List<Map<String, Object>> weatherRecords = weatherRepository.findWeatherByPincodeAndDate(pincode, date);

        if (!weatherRecords.isEmpty()) {
            return weatherRecords; // Return existing weather data if found
        }

        // If no records found, proceed to fetch data from the external API
        // (the rest of the code remains the same as before)
                 
        // Get latitude and longitude using the OpenWeather Geocoding API
        String geoUrl = String.format("https://api.openweathermap.org/geo/1.0/zip?zip=%s,IN&appid=%s", pincode, apiKey);
        PincodeLocation location = restTemplate.getForObject(geoUrl, PincodeLocation.class);
        
     // Log the location object received from the API
        System.out.println("Location from API: " + location); 
        
        if (location == null || location.getLatitude() == 0.0 || location.getLongitude() == 0.0) {
            throw new RuntimeException("Invalid pincode or location not found");
        }
        
     // Check if the location already exists in the database
        PincodeLocation existingLocation = pincodeLocationRepository.findById(pincode).orElse(null); //@
        if (existingLocation == null) { 
            System.out.println("Saving new location to the database: " + location);
            //Add the pincode location to the database
            savePincodeLocation(location, pincode);
            // Save the location to the database if not already present 
            location.setPincode(pincode); // Set the pincode before saving
            
        }

        // fetch weather info using latitude and longitude
        String weatherUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric",
                location.getLatitude(), location.getLongitude(), apiKey);
        
       
        //Initialize weatherResponse with the API response
        Map<String, Object> weatherResponse = restTemplate.getForObject(weatherUrl, Map.class);

        //Check if the response is not null
        if (weatherResponse == null) {
            throw new RuntimeException("Failed to fetch weather data from the external API");
        }

        //Instantiate WeatherInfo object
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setPincode(pincode);
        weatherInfo.setDate(date);

        //Extract weather description safely
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weatherResponse.get("weather");
        if (weatherList != null && !weatherList.isEmpty()) {
            String description = (String) weatherList.get(0).get("description");
            weatherInfo.setWeatherDescription(description);
        }

        //Extract temperature safely
        Map<String, Object> mainMap = (Map<String, Object>) weatherResponse.get("main");
        if (mainMap != null) {
            Double temp = (Double) mainMap.get("temp");
            if (temp != null) {
                weatherInfo.setTemperature(temp);
            }
        }

        // Save weather info to the database
        weatherRepository.save(weatherInfo);

        //Prepare the result map to be returned
        Map<String, Object> result = new HashMap<>();
        result.put("pincode", weatherInfo.getPincode());
        result.put("date", weatherInfo.getDate());
        result.put("weatherDescription", weatherInfo.getWeatherDescription());
        result.put("temperature", weatherInfo.getTemperature());

        //Changed the return type to a list for consistency.
        return List.of(result);
    }
    private void savePincodeLocation(PincodeLocation location, String pincode) {
        try {
            // Set the pincode before saving
            location.setPincode(pincode); // Set the actual pincode
            // Attempt to save the location to the database
            pincodeLocationRepository.save(location); // Save the new PincodeLocation
            System.out.println("Location saved successfully for pincode: " + pincode);
        } catch (Exception e) {
            System.out.println("Failed to save location: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
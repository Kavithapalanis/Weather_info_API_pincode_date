package com.example.weatherinfo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.weatherinfo.modelentity.WeatherInfo;
import com.example.weatherinfo.service.WeatherService;
import com.example.weatherinfo.modelentity.PincodeLocation; 
import com.example.weatherinfo.repository.PincodeLocationRepository;


@RestController
@RequestMapping("/api/weather")// postman Get request and response URL(http://localhost:8080/api/weather?pincode=411014&for_date=2020-10-15)
//https://api.openweathermap.org/geo/1.0/zip?zip=411014,IN&appid=e3ad39c082b678d3352d7d36098244c4 to get pincode table details on postman
public class WeatherController {
	
	private final WeatherService weatherService;
	private final PincodeLocationRepository pincodeLocationRepository; // Inject PincodeLocationRepository

    public WeatherController(WeatherService weatherService, PincodeLocationRepository pincodeLocationRepository) {
        this.weatherService = weatherService;
        this.pincodeLocationRepository = pincodeLocationRepository; // Initialize in constructor
    }
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getWeather(@RequestParam String pincode, @RequestParam String for_date) {
    	// Save pincode location here
        savePincodeLocation(pincode);
        
        List<Map<String, Object>> weatherInfo = weatherService.getWeatherInfo(pincode, for_date);
        return ResponseEntity.ok(weatherInfo);
    }
    
 
    
     private void savePincodeLocation(String pincode) {
        // Create a new PincodeLocation entity
        PincodeLocation location = new PincodeLocation();
        location.setPincode(pincode);
        // Set other properties if needed

        //to save the location using the repository
        pincodeLocationRepository.save(location);
    } 
}
    



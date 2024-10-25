package com.example.weatherinformation;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.weatherinfo.modelentity.WeatherInfo;
import com.example.weatherinfo.repository.WeatherRepository;
import com.example.weatherinfo.service.WeatherService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeatherServiceTests {
	
	@Mock
	private WeatherRepository weatherRepository;
	
	@InjectMocks
	private WeatherService weatherService;
	
	public WeatherServiceTests() {
        MockitoAnnotations.openMocks(this);
    }
	
	 @Test
	 public void testGetWeatherInfo_ExistingRecord() {
		 
		//Creating a WeatherInfo object to simulate existing record
	        WeatherInfo weatherInfo = new WeatherInfo();
	        weatherInfo.setPincode("411014");
	        weatherInfo.setDate("2020-10-15");
	       
	     // Update temperature and description in the mock data:
	        weatherInfo.setTemperature(25.75); 
	        weatherInfo.setWeatherDescription("broken clouds"); 

	        //Creating a list with a map to mock repository behavior
	        List<Map<String, Object>> existingRecords = new ArrayList<>();
	        Map<String, Object> mockMap = new HashMap<>();
	        mockMap.put("pincode", weatherInfo.getPincode());
	        mockMap.put("date", weatherInfo.getDate());
	        mockMap.put("temperature", weatherInfo.getTemperature());
	        mockMap.put("weatherDescription", weatherInfo.getWeatherDescription());
	        existingRecords.add(mockMap); //Adding mock record to the list

	        //Mocking the behavior of the weather repository
	        when(weatherRepository.findWeatherByPincodeAndDate(anyString(), anyString())).thenReturn(existingRecords);

	        //Calling the service method
	        List<Map<String, Object>> resultList = weatherService.getWeatherInfo("411014", "2020-10-15"); //Changed: Result type to List<Map<String, Object>>

	        //Assertions to verify the behavior
	        Map<String, Object> resultMap = resultList.get(0); //Get the first map from the list
	        WeatherInfo resultWeatherInfo = new WeatherInfo(); //Instantiate a new WeatherInfo
	        resultWeatherInfo.setPincode((String) resultMap.get("pincode"));
	        resultWeatherInfo.setDate((String) resultMap.get("date"));
	        resultWeatherInfo.setTemperature((Double) resultMap.get("temperature"));
	        resultWeatherInfo.setWeatherDescription((String) resultMap.get("weatherDescription"));

	        //Assertions to verify the WeatherInfo object
	       // assert resultWeatherInfo.getTemperature() == 25.75; //Verifying temperature
	        //assert "broken clouds".equals(resultWeatherInfo.getWeatherDescription()); //Verifying weather description
	     // Update assertions to match mock data:
	        assert resultWeatherInfo.getTemperature() == 25.75; // @ Verifying temperature
	        assert "broken clouds".equals(resultWeatherInfo.getWeatherDescription()); // @ Verifying weather description
	        assert "411014".equals(resultWeatherInfo.getPincode()); //Verifying pincode
	        assert "2020-10-15".equals(resultWeatherInfo.getDate()); //Verifying date
	    }
		
}

package com.example.weatherinfo.repository;

import com.example.weatherinfo.modelentity.WeatherInfo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface WeatherRepository extends JpaRepository<WeatherInfo, Long>  {
	//WeatherInfo findByPincodeAndDate(String pincode, String date);
    @Query("SELECT new map(w.pincode as pincode, w.date as date, w.weatherDescription as weatherDescription, w.temperature as temperature) " +
            "FROM WeatherInfo w WHERE w.pincode = :pincode AND w.date = :date")
     List<Map<String, Object>> findWeatherByPincodeAndDate(String pincode, String date);
	

}

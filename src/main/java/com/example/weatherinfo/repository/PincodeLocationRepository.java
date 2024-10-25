package com.example.weatherinfo.repository;

import com.example.weatherinfo.modelentity.PincodeLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PincodeLocationRepository extends JpaRepository<PincodeLocation, String> {
	
	

}

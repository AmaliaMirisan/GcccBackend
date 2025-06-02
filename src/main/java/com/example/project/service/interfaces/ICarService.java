package com.example.project.service.interfaces;

import com.example.project.domain.dto.CarDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ICarService {
    List<CarDTO> findAll();
    CarDTO save(CarDTO carDTO) throws IOException;
    void deleteCar(Long id);
    CarDTO getCarById(Long id);
    CarDTO updateCar(CarDTO carDTO) throws IOException;
}

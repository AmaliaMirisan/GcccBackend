package com.example.project.controller;

import com.example.project.domain.dto.CarDTO;
import com.example.project.service.interfaces.ICarService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/cars")
public class CarController {

    @Resource
    private final ICarService carService;

    public CarController(ICarService service) {this.carService =  service; }

    @GetMapping("/")
    public ResponseEntity<List<CarDTO>> getAllCars(){
        List<CarDTO> carDTOS = carService.findAll();
        if (carDTOS != null) {
            return new ResponseEntity<>(carDTOS, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<?> addCar(@ModelAttribute CarDTO carDTO) throws IOException {
        return ResponseEntity.ok(this.carService.save(carDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
        CarDTO carDTO = carService.getCarById(id);
        if (carDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @ModelAttribute CarDTO car) throws IOException {
        CarDTO existingCarDTO = carService.getCarById(id);
        if (existingCarDTO == null) {
            return ResponseEntity.notFound().build();
        }

        if (car.getImage() == null || car.getImage().isEmpty()) {
            car.setImageUrl(existingCarDTO.getImageUrl());
        }
        car.setId(id);

        return ResponseEntity.ok(carService.updateCar(car));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id){
        CarDTO existingCar = carService.getCarById(id);
        if (existingCar == null) {
            return ResponseEntity.notFound().build();
        }
        carService.deleteCar(id);
        return ResponseEntity.ok().build();
    }
}

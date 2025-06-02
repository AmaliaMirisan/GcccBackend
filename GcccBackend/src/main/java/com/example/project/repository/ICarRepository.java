package com.example.project.repository;

import com.example.project.domain.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICarRepository extends JpaRepository<Car,Long> {
    List<Car> findAll();
}

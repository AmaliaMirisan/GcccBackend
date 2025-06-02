package com.example.project.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.project.domain.dto.CarDTO;
import com.example.project.domain.model.Car;
import com.example.project.repository.ICarRepository;
import com.example.project.service.interfaces.ICarService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CarService implements ICarService {

    @Resource
    private final ICarRepository carRepository;

    public CarService(ICarRepository carRepository) {this.carRepository = carRepository;}

    @Override
    public List<CarDTO> findAll(){
        List<Car> cars = carRepository.findAll();
        List<CarDTO> carDTOs = new ArrayList<>();

        for (Car car : cars){
            CarDTO carDTO = new CarDTO();
            carDTO.setId(car.getId());
            carDTO.setModel(car.getModel());
            carDTO.setManufacturer(car.getManufacturer());
            carDTO.setProductionYear(car.getProductionYear());
            carDTO.setImageUrl(car.getImageUrl());
            carDTOs.add(carDTO);
        }
        return carDTOs;
    }

    @Override
    public CarDTO save(CarDTO carDTO) throws IOException {
        String uniqueBlobName = generateUniqueBlobName();

        String blobUrl = constructBlobUrl(uniqueBlobName);

        uploadImageToBlobStorage(carDTO.getImage().getBytes(), uniqueBlobName, "gcccblob", "photos", "jomRx8nJzBqhgV7LtuatYyRrgumyaMp/ghCC+fp8E9XmFM3oeifQRj4aPrSfS8JHygWesydpG5Or+AStRSs0QA==");

        Car car = new Car();
        car.setId(carDTO.getId());
        car.setModel(carDTO.getModel());
        car.setManufacturer(carDTO.getManufacturer());
        car.setProductionYear(carDTO.getProductionYear());
        car.setImageUrl(blobUrl);
        Car addedCar = this.carRepository.save(car);
        return this.generateCarDtoFromEntity(addedCar);
    }

    public CarDTO getCarById(Long id){
        Car car =  carRepository.findById(id).orElse(null);
        CarDTO carDTO= new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setModel(car.getModel());
        carDTO.setManufacturer(car.getManufacturer());
        carDTO.setProductionYear(car.getProductionYear());
        carDTO.setImageUrl(car.getImageUrl());
        return carDTO;
    }

    public CarDTO updateCar(CarDTO carDTO) throws IOException {
        Car existingCar = carRepository.findById(carDTO.getId()).orElse(null);
        if (existingCar == null) return null;

        existingCar.setModel(carDTO.getModel());
        existingCar.setManufacturer(carDTO.getManufacturer());
        existingCar.setProductionYear(carDTO.getProductionYear());

        if (carDTO.getImage() != null && !carDTO.getImage().isEmpty()) {
            String uniqueBlobName = generateUniqueBlobName();
            uploadImageToBlobStorage(carDTO.getImage().getBytes(), uniqueBlobName, "gcccblob", "photos", "jomRx8nJzBqhgV7LtuatYyRrgumyaMp/ghCC+fp8E9XmFM3oeifQRj4aPrSfS8JHygWesydpG5Or+AStRSs0QA==");
            String blobUrl = constructBlobUrl(uniqueBlobName);
            existingCar.setImageUrl(blobUrl);
        }

        Car updatedCar = carRepository.save(existingCar);
        return generateCarDtoFromEntity(updatedCar);
    }


    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }

    private String generateUniqueBlobName() {
        return UUID.randomUUID() + ".jpg";
    }

    private String constructBlobUrl(String uniqueBlobName) {
        String baseUrl = "https://gcccblob.blob.core.windows.net/";
        String containerName = "photos";

        return baseUrl + containerName + "/" + uniqueBlobName;
    }

    public String uploadImageToBlobStorage(byte[] imageData, String imageNameParam, String accountName, String containerName, String accountKey) {
        BlobServiceClientBuilder builder = new BlobServiceClientBuilder()
                .connectionString("DefaultEndpointsProtocol=https;AccountName=" + accountName + ";AccountKey=" + accountKey + ";EndpointSuffix=core.windows.net");
        BlobContainerClient containerClient = builder.buildClient().getBlobContainerClient(containerName);

        InputStream inputStream = new ByteArrayInputStream(imageData);

        BlobClient blobClient = containerClient.getBlobClient(imageNameParam);
        blobClient.upload(inputStream, imageData.length);

        return "https://" + accountName + ".blob.core.windows.net/" + containerName + "/" + imageNameParam;
    }

    public static CarDTO generateCarDtoFromEntity(Car car) {
        return CarDTO.builder()
                .id(car.getId())
                .model(car.getModel())
                .manufacturer(car.getManufacturer())
                .productionYear(car.getProductionYear())
                .imageUrl(car.getImageUrl())
                .build();
    }
}

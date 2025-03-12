package org.example;

import repository.CarsDBRepository;
import model.Car;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainBD {
    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(new FileReader("C:\\Users\\emanu\\IdeaProjects\\teme-lab-Marcu21\\lab3\\db.config"));
        } catch (IOException e) {
            System.out.println("no file found: " + e);
            return;
        }

        CarsDBRepository carRepo = new CarsDBRepository(props);

        Car mercedes1 = new Car("Mercedes", "E Coupe", 2018);
        carRepo.add(mercedes1);

        Car mercedes2 = new Car("Mercedes", "CLS", 2020);
        carRepo.add(mercedes2);

        mercedes1.setYear(2019);
        carRepo.update(mercedes1, mercedes1.getID());

        String brand = "Mercedes";
        System.out.println("masini " + brand + ":");
        for (Car car : carRepo.findAll()) {
            if (car.getManufacturer().equals(brand)) {
                System.out.println(car);
            }
        }
        carRepo.delete(mercedes1);
        carRepo.delete(mercedes2);
    }
}

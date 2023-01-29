package com.driver.model;

import java.util.List;

import javax.persistence.*;

@Entity
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;
    String address;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    List<Spot> spotList;

    public ParkingLot() {
    }

    public ParkingLot(int id, String name, String address, List<Spot> spotList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.spotList = spotList;
    }

    public ParkingLot(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public ParkingLot(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Spot> getSpotList() {
        return spotList;
    }

    public void setSpotList(List<Spot> spotList) {
        this.spotList = spotList;
    }

}

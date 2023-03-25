package com.fleetenable.billing.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class EmbeddedAccountAddress {
  private String address_line1;
  private String address_line2;
  private String zipcode;
  private String city;
  private String state;
  private String country;
  private List<Double> coordinates;

  public String getAddress_line1() {
    return address_line1;
  }
  public void setAddress_line1(String address_line1) {
    this.address_line1 = address_line1;
  }
  public String getAddress_line2() {
    return address_line2;
  }
  public void setAddress_line2(String address_line2) {
    this.address_line2 = address_line2;
  }
  public String getZipcode() {
    return zipcode;
  }
  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }
  public String getCity() {
    return city;
  }
  public void setCity(String city) {
    this.city = city;
  }
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }
  public String getCountry() {
    return country;
  }
  public void setCountry(String country) {
    this.country = country;
  }
  public List<Double> getCoordinates() {
    return coordinates;
  }
  public void setCoordinates(List<Double> coordinates) {
    this.coordinates = coordinates;
  }
  
}

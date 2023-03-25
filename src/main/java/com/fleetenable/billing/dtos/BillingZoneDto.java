package com.fleetenable.billing.dtos;

import java.util.List;

public class BillingZoneDto {
  private String name;
  private Double max_distance;
  private Double min_distance;
  private List<String> zip_codes;
  private String id;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Double getMax_distance() {
    return max_distance;
  }
  public void setMax_distance(Double max_distance) {
    this.max_distance = max_distance;
  }
  public Double getMin_distance() {
    return min_distance;
  }
  public void setMin_distance(Double min_distance) {
    this.min_distance = min_distance;
  }
  public List<String> getZip_codes() {
    return zip_codes;
  }
  public void setZip_codes(List<String> zip_codes) {
    this.zip_codes = zip_codes;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
}

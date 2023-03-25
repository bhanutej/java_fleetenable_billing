package com.fleetenable.billing.dtos.sub_entities;

public class WeightParamValuesDto {
  private String accessorial_key;
  private String accessorial_value;
  
  public WeightParamValuesDto(String accessorial_key, String accessorial_value) {
    this.accessorial_key = accessorial_key;
    this.accessorial_value = accessorial_value;
  }
  public String getAccessorial_key() {
    return accessorial_key;
  }
  public void setAccessorial_key(String accessorial_key) {
    this.accessorial_key = accessorial_key;
  }
  public String getAccessorial_value() {
    return accessorial_value;
  }
  public void setAccessorial_value(String accessorial_value) {
    this.accessorial_value = accessorial_value;
  }

}

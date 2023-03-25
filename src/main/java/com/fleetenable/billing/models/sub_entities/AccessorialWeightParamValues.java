package com.fleetenable.billing.models.sub_entities;

public class AccessorialWeightParamValues {
  private String accessorial_key;
  private String accessorial_value;
  private String zoine_id;
  
  public AccessorialWeightParamValues(String accessorial_key, String accessorial_value, String zoine_id) {
    this.accessorial_key = accessorial_key;
    this.accessorial_value = accessorial_value;
    this.zoine_id = zoine_id;
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
  public String getZoine_id() {
    return zoine_id;
  }
  public void setZoine_id(String zoine_id) {
    this.zoine_id = zoine_id;
  }
  
}

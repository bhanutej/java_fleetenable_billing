package com.fleetenable.billing.models.sub_entities;

public class AccessorialWeightParamValues {
  private String accessorial_key;
  private String accessorial_value;
  private String zone_id = null;
  
  public AccessorialWeightParamValues(String accessorial_key, String accessorial_value, String zone_id) {
    this.accessorial_key = accessorial_key;
    this.accessorial_value = accessorial_value;
    this.zone_id = zone_id;
  }

  public AccessorialWeightParamValues() {
    
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
  public String getZone_id() {
    return zone_id;
  }
  public void setZone_id(String zone_id) {
    this.zone_id = zone_id;
  }
  
}

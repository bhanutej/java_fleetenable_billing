package com.fleetenable.billing.dtos;

import java.util.List;

public class BillingZoneRequestDto {
  private String account_id;
  private String zone_type;
  private List<BillingZoneDto> zones;

  public String getAccount_id() {
    return account_id;
  }
  public String getZone_type() {
    return zone_type;
  }
  public List<BillingZoneDto> getZones() {
    return zones;
  }
  
  public void setAccount_id(String account_id) {
    this.account_id = account_id;
  }
  public void setZone_type(String zone_type) {
    this.zone_type = zone_type;
  }
  public void setZones(List<BillingZoneDto> zones) {
    this.zones = zones;
  }
}

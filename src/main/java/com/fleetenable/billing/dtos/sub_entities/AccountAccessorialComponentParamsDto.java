package com.fleetenable.billing.dtos.sub_entities;

import java.util.List;

public class AccountAccessorialComponentParamsDto {
  private String component_code;
  private String zone_category;
  private List<AccessorialParamsDto> accessorial_params;
  
  public AccountAccessorialComponentParamsDto(String component_code, List<AccessorialParamsDto> accessorial_params) {
    this.component_code = component_code;
    this.accessorial_params = accessorial_params;
  }

  public String getComponent_code() {
    return component_code;
  }
  public void setComponent_code(String component_code) {
    this.component_code = component_code;
  }
  public List<AccessorialParamsDto> getAccessorial_params() {
    return accessorial_params;
  }
  public void setAccessorial_params(List<AccessorialParamsDto> accessorial_params) {
    this.accessorial_params = accessorial_params;
  }
  public String getZone_category() {
    return zone_category;
  }

  public void setZone_category(String zone_category) {
    this.zone_category = zone_category;
  }
}

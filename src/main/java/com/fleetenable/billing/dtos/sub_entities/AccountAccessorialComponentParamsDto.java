package com.fleetenable.billing.dtos.sub_entities;

import java.util.List;

public class AccountAccessorialComponentParamsDto {
  private String component_code;
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
}

package com.fleetenable.billing.dtos;

import java.util.List;

import com.fleetenable.billing.dtos.sub_entities.AccountAccessorialComponentParamsDto;

public class AccountAccessorialParamDto {
  private String code;
  private String account_id;
  private String organization_id;
  private List<AccountAccessorialComponentParamsDto> component_params;
  
  public AccountAccessorialParamDto(String code, String account_id, String organization_id,
      List<AccountAccessorialComponentParamsDto> component_params) {
    this.code = code;
    this.account_id = account_id;
    this.organization_id = organization_id;
    this.component_params = component_params;
  }
  
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getAccount_id() {
    return account_id;
  }
  public void setAccount_id(String account_id) {
    this.account_id = account_id;
  }
  public String getOrganization_id() {
    return organization_id;
  }
  public void setOrganization_id(String organization_id) {
    this.organization_id = organization_id;
  }
  public List<AccountAccessorialComponentParamsDto> getComponent_params() {
    return component_params;
  }
  public void setComponent_params(List<AccountAccessorialComponentParamsDto> component_params) {
    this.component_params = component_params;
  }
}

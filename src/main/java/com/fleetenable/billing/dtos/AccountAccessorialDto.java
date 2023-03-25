package com.fleetenable.billing.dtos;

import java.util.List;

import com.fleetenable.billing.models.sub_entities.AccountAccessorialComponent;

public class AccountAccessorialDto {
  
  private String id;
  private List<String> accessible_to;
  private String name;
  private String code;
  private String gl_code;
  private String rate_source;
  private String resources;
  private String account_id;
  private String organization_id;
  private Object accessorial_config_params;
  private List<AccountAccessorialComponent> components;

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public List<String> getAccessible_to() {
    return accessible_to;
  }
  public void setAccessible_to(List<String> accessible_to) {
    this.accessible_to = accessible_to;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getGl_code() {
    return gl_code;
  }
  public void setGl_code(String gl_code) {
    this.gl_code = gl_code;
  }
  public String getRate_source() {
    return rate_source;
  }
  public void setRate_source(String rate_source) {
    this.rate_source = rate_source;
  }
  public String getResources() {
    return resources;
  }
  public void setResources(String resources) {
    this.resources = resources;
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
  public Object getAccessorial_config_params() {
    return accessorial_config_params;
  }
  public void setAccessorial_config_params(Object accessorial_config_params) {
    this.accessorial_config_params = accessorial_config_params;
  }
  public List<AccountAccessorialComponent> getComponents() {
    return components;
  }
  public void setComponents(List<AccountAccessorialComponent> components) {
    this.components = components;
  }
}

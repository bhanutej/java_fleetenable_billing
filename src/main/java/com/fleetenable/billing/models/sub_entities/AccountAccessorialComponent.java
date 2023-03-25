package com.fleetenable.billing.models.sub_entities;

public class AccountAccessorialComponent {
  private String component_id;
  private String component_name;
  private String component_code;

  public AccountAccessorialComponent(String component_id, String component_name, String component_code) {
    this.component_id = component_id;
    this.component_name = component_name;
    this.component_code = component_code;
  }
  
  public String getComponent_id() {
    return component_id;
  }
  public void setComponent_id(String component_id) {
    this.component_id = component_id;
  }
  public String getComponent_name() {
    return component_name;
  }
  public void setComponent_name(String component_name) {
    this.component_name = component_name;
  }
  public String getComponent_code() {
    return component_code;
  }
  public void setComponent_code(String component_code) {
    this.component_code = component_code;
  }

  
}
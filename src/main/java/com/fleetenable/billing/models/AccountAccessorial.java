package com.fleetenable.billing.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.fleetenable.billing.models.sub_entities.AccountAccessorialComponent;

@Document(collection = "account_accessorials")
public class AccountAccessorial {
  @Id
  private String id;
  private String account_id;
  private String organization_id;
  private String accessorial_name;
  private String accessorial_code;
  private Boolean enable;
  private String gl_code;
  private String standard_code;
  private String is_clone;
  private Object accessorial_config_params;
  private String accounting_product_id;
  private List<String> accessible_to;
  private List<AccountAccessorialComponent> components;
  
  @CreationTimestamp
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime created_at;
  
  @UpdateTimestamp
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime updated_at;

  public AccountAccessorial(String account_id, String organization_id, String accessorial_code) {
    this.account_id = account_id;
    this.organization_id = organization_id;
    this.accessorial_code = accessorial_code;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getAccessorial_name() {
    return accessorial_name;
  }

  public void setAccessorial_name(String accessorial_name) {
    this.accessorial_name = accessorial_name;
  }

  public String getAccessorial_code() {
    return accessorial_code;
  }

  public void setAccessorial_code(String accessorial_code) {
    this.accessorial_code = accessorial_code;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public String getGl_code() {
    return gl_code;
  }

  public void setGl_code(String gl_code) {
    this.gl_code = gl_code;
  }

  public String getStandard_code() {
    return standard_code;
  }

  public void setStandard_code(String standard_code) {
    this.standard_code = standard_code;
  }

  public String getIs_clone() {
    return is_clone;
  }

  public void setIs_clone(String is_clone) {
    this.is_clone = is_clone;
  }

  public Object getAccessorial_config_params() {
    return accessorial_config_params;
  }

  public void setAccessorial_config_params(Object accessorial_config_params) {
    this.accessorial_config_params = accessorial_config_params;
  }

  public String getAccounting_product_id() {
    return accounting_product_id;
  }

  public void setAccounting_product_id(String accounting_product_id) {
    this.accounting_product_id = accounting_product_id;
  }

  public List<String> getAccessible_to() {
    return accessible_to;
  }

  public void setAccessible_to(List<String> accessible_to) {
    this.accessible_to = accessible_to;
  }

  public List<AccountAccessorialComponent> getComponents() {
    return components;
  }

  public void setComponents(List<AccountAccessorialComponent> components) {
    this.components = components;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  public LocalDateTime getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(LocalDateTime updated_at) {
    this.updated_at = updated_at;
  }
}

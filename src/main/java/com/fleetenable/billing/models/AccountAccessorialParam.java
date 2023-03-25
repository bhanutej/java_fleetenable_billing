package com.fleetenable.billing.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fleetenable.billing.models.sub_entities.AccessorialWeightParams;

import jakarta.persistence.Id;

@Document(collection = "account_accessorial_params")
public class AccountAccessorialParam {
  
  @Id
  private String id;
  private String account_id;
  private String organization_id;
  private String code;
  private String params_with_wt;
  private List<AccessorialWeightParams> accessorial_weight_params;

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
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getParams_with_wt() {
    return params_with_wt;
  }
  public void setParams_with_wt(String params_with_wt) {
    this.params_with_wt = params_with_wt;
  }
  public List<AccessorialWeightParams> getAccessorial_weight_params() {
    return accessorial_weight_params;
  }
  public void setAccessorial_weight_params(List<AccessorialWeightParams> accessorial_weight_params) {
    this.accessorial_weight_params = accessorial_weight_params;
  }
}

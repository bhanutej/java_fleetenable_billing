package com.fleetenable.billing.models.sub_entities;

import java.util.List;

public class AccessorialWeightParams {
  private String min_weight;
  private String max_weight;
  private String component_code;
  private String order_type;
  private String zone_category;
  private List<AccessorialWeightParamValues> param_values;

  public AccessorialWeightParams(String min_weight, String max_weight, String component_code, String order_type,
      String zone_category, List<AccessorialWeightParamValues> param_values) {
    this.min_weight = min_weight;
    this.max_weight = max_weight;
    this.component_code = component_code;
    this.order_type = order_type;
    this.zone_category = zone_category;
    this.param_values = param_values;
  }

  public String getMin_weight() {
    return min_weight;
  }
  public void setMin_weight(String min_weight) {
    this.min_weight = min_weight;
  }
  public String getMax_weight() {
    return max_weight;
  }
  public void setMax_weight(String max_weight) {
    this.max_weight = max_weight;
  }
  public String getComponent_code() {
    return component_code;
  }
  public void setComponent_code(String component_code) {
    this.component_code = component_code;
  }
  public String getOrder_type() {
    return order_type;
  }
  public void setOrder_type(String order_type) {
    this.order_type = order_type;
  }
  public String getZone_category() {
    return zone_category;
  }
  public void setZone_category(String zone_category) {
    this.zone_category = zone_category;
  }
  public List<AccessorialWeightParamValues> getParam_values() {
    return param_values;
  }
  public void setParam_values(List<AccessorialWeightParamValues> param_values) {
    this.param_values = param_values;
  }
}

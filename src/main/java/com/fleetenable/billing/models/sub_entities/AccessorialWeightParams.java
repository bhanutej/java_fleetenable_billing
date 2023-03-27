package com.fleetenable.billing.models.sub_entities;

import java.util.List;

public class AccessorialWeightParams {
  private Integer min_weight;
  private Integer max_weight;
  private String component_code;
  private String order_type;
  private Float breakpoint_weight;
  private String zone_category = "ALL_ZONES";
  private List<AccessorialWeightParamValues> param_values;

  public AccessorialWeightParams(Integer min_weight, Integer max_weight, String component_code, String order_type,
      String zone_category, List<AccessorialWeightParamValues> param_values) {
    this.min_weight = min_weight;
    this.max_weight = max_weight;
    this.component_code = component_code;
    this.order_type = order_type;
    this.zone_category = zone_category;
    this.param_values = param_values;
  }

  public AccessorialWeightParams() {
  }

  public Integer getMin_weight() {
    return min_weight;
  }
  public void setMin_weight(Integer min_weight) {
    this.min_weight = min_weight;
  }
  public Integer getMax_weight() {
    return max_weight;
  }
  public void setMax_weight(Integer max_weight) {
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

  public Float getBreakpoint_weight() {
    return breakpoint_weight;
  }

  public void setBreakpoint_weight(Float breakpoint_weight) {
    this.breakpoint_weight = breakpoint_weight;
  }
}

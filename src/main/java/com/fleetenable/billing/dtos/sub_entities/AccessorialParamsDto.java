package com.fleetenable.billing.dtos.sub_entities;

import java.util.List;

public class AccessorialParamsDto {
  private String params_with_wt;
  private String min_weight;
  private String max_weight;
  private String order_type;
  private List<WeightParamValuesDto> wt_param_values;
  
  public AccessorialParamsDto(String params_with_wt, String min_weight, String max_weight, String order_type,
      List<WeightParamValuesDto> wt_param_values) {
    this.params_with_wt = params_with_wt;
    this.min_weight = min_weight;
    this.max_weight = max_weight;
    this.order_type = order_type;
    this.wt_param_values = wt_param_values;
  }
  
  public String getParams_with_wt() {
    return params_with_wt;
  }
  public void setParams_with_wt(String params_with_wt) {
    this.params_with_wt = params_with_wt;
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
  public String getOrder_type() {
    return order_type;
  }
  public void setOrder_type(String order_type) {
    this.order_type = order_type;
  }
  public List<WeightParamValuesDto> getWt_param_values() {
    return wt_param_values;
  }
  public void setWt_param_values(List<WeightParamValuesDto> wt_param_values) {
    this.wt_param_values = wt_param_values;
  }
}

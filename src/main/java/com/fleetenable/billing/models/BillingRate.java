package com.fleetenable.billing.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "billing_rates")
public class BillingRate {
  @Id
  private String id;
  private String account_id;
  private String organization_id;
  private String zone_id;
  private String units;
  private Float min_weight;
  private Float max_weight;
  private Float breakpoint_weight;
  private String amount;
  private String level_of_service_id;
  private String apply_cwt;
  private String range_type;

  @CreationTimestamp
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime created_at;
  
  @UpdateTimestamp
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime updated_at;
  
  public BillingRate(String account_id, String organization_id, String zone_id, Float min_weight, Float max_weight) {
    this.account_id = account_id;
    this.organization_id = organization_id;
    this.zone_id = zone_id;
    this.min_weight = min_weight;
    this.max_weight = max_weight;
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
  public String getZone_id() {
    return zone_id;
  }
  public void setZone_id(String zone_id) {
    this.zone_id = zone_id;
  }
  public String getUnits() {
    return units;
  }
  public void setUnits(String units) {
    this.units = units;
  }
  public Float getMin_weight() {
    return min_weight;
  }
  public void setMin_weight(Float min_weight) {
    this.min_weight = min_weight;
  }
  public Float getMax_weight() {
    return max_weight;
  }
  public void setMax_weight(Float max_weight) {
    this.max_weight = max_weight;
  }
  public Float getBreakpoint_weight() {
    return breakpoint_weight;
  }
  public void setBreakpoint_weight(Float breakpoint_weight) {
    this.breakpoint_weight = breakpoint_weight;
  }
  public String getAmount() {
    return amount;
  }
  public void setAmount(String amount) {
    this.amount = amount;
  }
  public String getLevel_of_service_id() {
    return level_of_service_id;
  }
  public void setLevel_of_service_id(String level_of_service_id) {
    this.level_of_service_id = level_of_service_id;
  }
  public String getApply_cwt() {
    return apply_cwt;
  }
  public void setApply_cwt(String apply_cwt) {
    this.apply_cwt = apply_cwt;
  }
  public String getRange_type() {
    return range_type;
  }
  public void setRange_type(String range_type) {
    this.range_type = range_type;
  }

  
}

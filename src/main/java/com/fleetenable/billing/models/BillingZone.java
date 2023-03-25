package com.fleetenable.billing.models;

import java.util.List;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "billing_zones")
public class BillingZone {
  @Id
  private String id;
  private String name;
  private String zone_type;
  private String account_id;
  private String organization_id;
  private Double min_distance;
  private Double max_distance;
  private List<String> zip_codes;
  private List<String> zip_codes_to_compare;

  @CreationTimestamp
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime created_at;
  
  @UpdateTimestamp
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime updated_at;

  @PersistenceCreator
  public BillingZone(String id, String name, String zone_type, String account_id, String organization_id,
      Double min_distance, Double max_distance, List<String> zip_codes, List<String> zip_codes_to_compare) {
    this.id = id;
    this.name = name;
    this.zone_type = zone_type;
    this.account_id = account_id;
    this.organization_id = organization_id;
    this.min_distance = min_distance;
    this.max_distance = max_distance;
    this.zip_codes = zip_codes;
    this.zip_codes_to_compare = zip_codes_to_compare;
  }
  
  public BillingZone(String name, String zone_type, String account_id, String organization_id) {
    this.name = name;
    this.zone_type = zone_type;
    this.account_id = account_id;
    this.organization_id = organization_id;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getZone_type() {
    return zone_type;
  }
  public void setZone_type(String zone_type) {
    this.zone_type = zone_type;
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
  public Double getMin_distance() {
    return min_distance;
  }
  public void setMin_distance(Double min_distance) {
    this.min_distance = min_distance;
  }
  public Double getMax_distance() {
    return max_distance;
  }
  public void setMax_distance(Double max_distance) {
    this.max_distance = max_distance;
  }
  public List<String> getZip_codes() {
    return zip_codes;
  }
  public void setZip_codes(List<String> zip_codes) {
    this.zip_codes = zip_codes;
  }
  public List<String> getZip_codes_to_compare() {
    return zip_codes_to_compare;
  }
  public void setZip_codes_to_compare(List<String> zip_codes_to_compare) {
    this.zip_codes_to_compare = zip_codes_to_compare;
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

package com.fleetenable.billing.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "accounts")
public class Account {
  @Id
  private String id;
  private String name;
  private String code;
  private String organization_id;
  private String phone_number;
  private String email;
  private String contact_person;
  
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime deleted_at;
  
  @CreationTimestamp
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime created_at;
  
  @UpdateTimestamp
  @Field(targetType = FieldType.DATE_TIME)
  private LocalDateTime updated_at;
  
  private String billing_template;
  private String primary_account_id;
  private String primary_account_code;
  private List<String> manager_emails;
  private Boolean org_delivery_zip;
  private List<String> warehouse_ids;
  private String integration_code;
  private String credit_limit_amount;
  private String credit_due_amount;

  @Field("address")
  private EmbeddedAccountAddress address;
  
  @Field("billing_address")
  private EmbeddedAccountAddress billing_address;

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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getOrganization_id() {
    return organization_id;
  }

  public void setOrganization_id(String organization_id) {
    this.organization_id = organization_id;
  }

  public String getPhone_number() {
    return phone_number;
  }

  public void setPhone_number(String phone_number) {
    this.phone_number = phone_number;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getContact_person() {
    return contact_person;
  }

  public void setContact_person(String contact_person) {
    this.contact_person = contact_person;
  }

  public LocalDateTime getDeleted_at() {
    return deleted_at;
  }

  public void setDeleted_at(LocalDateTime deleted_at) {
    this.deleted_at = deleted_at;
  }

  public String getBilling_template() {
    return billing_template;
  }

  public void setBilling_template(String billing_template) {
    this.billing_template = billing_template;
  }

  public String getPrimary_account_id() {
    return primary_account_id;
  }

  public void setPrimary_account_id(String primary_account_id) {
    this.primary_account_id = primary_account_id;
  }

  public String getPrimary_account_code() {
    return primary_account_code;
  }

  public void setPrimary_account_code(String primary_account_code) {
    this.primary_account_code = primary_account_code;
  }

  public List<String> getManager_emails() {
    return manager_emails;
  }

  public void setManager_emails(List<String> manager_emails) {
    this.manager_emails = manager_emails;
  }

  public Boolean getOrg_delivery_zip() {
    return org_delivery_zip;
  }

  public void setOrg_delivery_zip(Boolean org_delivery_zip) {
    this.org_delivery_zip = org_delivery_zip;
  }

  public List<String> getWarehouse_ids() {
    return warehouse_ids;
  }

  public void setWarehouse_ids(List<String> warehouse_ids) {
    this.warehouse_ids = warehouse_ids;
  }

  public String getIntegration_code() {
    return integration_code;
  }

  public void setIntegration_code(String integration_code) {
    this.integration_code = integration_code;
  }

  public String getCredit_limit_amount() {
    return credit_limit_amount;
  }

  public void setCredit_limit_amount(String credit_limit_amount) {
    this.credit_limit_amount = credit_limit_amount;
  }

  public String getCredit_due_amount() {
    return credit_due_amount;
  }

  public void setCredit_due_amount(String credit_due_amount) {
    this.credit_due_amount = credit_due_amount;
  }

  public EmbeddedAccountAddress getAddress() {
    return address;
  }

  public void setAddress(EmbeddedAccountAddress address) {
    this.address = address;
  }

  public EmbeddedAccountAddress getBilling_address() {
    return billing_address;
  }

  public void setBilling_address(EmbeddedAccountAddress billing_address) {
    this.billing_address = billing_address;
  }
}

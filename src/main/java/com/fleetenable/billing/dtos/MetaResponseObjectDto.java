package com.fleetenable.billing.dtos;

public class MetaResponseObjectDto {
  private Integer current_page;
  private Integer next_page;
  private Integer prev_page;
  private Integer total_pages;
  private Integer total_count;
  
  public MetaResponseObjectDto(Integer current_page, Integer next_page, Integer prev_page, Integer total_pages, Integer total_count) {
    this.current_page = current_page;
    this.next_page = next_page;
    this.prev_page = prev_page;
    this.total_pages = total_pages;
    this.total_count = total_count;
  }

  public Integer getCurrent_page() {
    return current_page;
  }
  public void setCurrent_page(Integer current_page) {
    this.current_page = current_page;
  }
  public Integer getNext_page() {
    return next_page;
  }
  public void setNext_page(Integer next_page) {
    this.next_page = next_page;
  }
  public Integer getPrev_page() {
    return prev_page;
  }
  public void setPrev_page(Integer prev_page) {
    this.prev_page = prev_page;
  }
  public Integer getTotal_pages() {
    return total_pages;
  }
  public void setTotal_pages(Integer total_pages) {
    this.total_pages = total_pages;
  }
  public Integer getTotal_count() {
    return total_count;
  }
  public void setTotal_count(Integer total_count) {
    this.total_count = total_count;
  }  
}

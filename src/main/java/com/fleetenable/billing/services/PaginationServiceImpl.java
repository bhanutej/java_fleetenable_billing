package com.fleetenable.billing.services;

import com.fleetenable.billing.services.interfaces.Pagination;

public class PaginationServiceImpl implements Pagination {
    
  private int totalItems;
  private int itemsPerPage;
  private int currentPage;
  
  public PaginationServiceImpl(int totalItems, int itemsPerPage, int currentPage) {
      this.totalItems = totalItems;
      this.itemsPerPage = itemsPerPage;
      this.currentPage = currentPage;
  }
  
  @Override
  public int getTotalItems() {
      return totalItems;
  }
  
  @Override
  public int getItemsPerPage() {
      return itemsPerPage;
  }
  
  @Override
  public int getTotalPages() {
      int totalPages = totalItems / itemsPerPage;
      if (totalItems % itemsPerPage != 0) {
          totalPages++;
      }
      return totalPages;
  }
  
  @Override
  public int getCurrentPage() {
      return currentPage;
  }

  @Override
  public Integer getNextPage() {
    if (getTotalPages() - currentPage > 0) {
      return currentPage + 1;
    }
    return null;
  }
  
  @Override
  public Integer getPrevPage() {
    if (currentPage - 1 > 0) {
      return currentPage - 1;
    }
    return null;
  }
  
  @Override
  public void setCurrentPage(int pageNumber) {
      if (pageNumber < 1 || pageNumber > getTotalPages()) {
          throw new IllegalArgumentException("Invalid page number");
      }
      currentPage = pageNumber;
  }
  
  @Override
  public int getStartingIndex() {
      return (currentPage - 1) * itemsPerPage;
  }
  
  @Override
  public int getEndingIndex() {
      int endIndex = currentPage * itemsPerPage - 1;
      return endIndex >= totalItems ? totalItems - 1 : endIndex;
  }
}

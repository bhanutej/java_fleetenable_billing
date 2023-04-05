package com.fleetenable.billing.services.interfaces;

public interface Pagination {
  /**
     * Returns the total number of items in the list.
     */
    int getTotalItems();
    
    /**
     * Returns the number of items to be displayed per page.
     */
    int getItemsPerPage();
    
    /**
     * Returns the total number of pages based on the total items and items per page.
     */
    int getTotalPages();
    
    /**
     * Returns the current page number.
     */
    int getCurrentPage();
    
    /**
     * Returns the next page number.
     */
    Integer getNextPage();
    
    /**
     * Returns the prev page number.
     */
    Integer getPrevPage();
    
    /**
     * Sets the current page number.
     */
    void setCurrentPage(int pageNumber);
    
    /**
     * Returns the starting index of the current page.
     */
    int getStartingIndex();
    
    /**
     * Returns the ending index of the current page.
     */
    int getEndingIndex();
}

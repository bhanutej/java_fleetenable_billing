package com.fleetenable.billing.services;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fleetenable.billing.dtos.MetaResponseObjectDto;

@Component
public class CommonServices {
  
  public void paginationResponse(Map<String, Object> response, String page, String perPage, int totalSize) {
    PaginationServiceImpl pagination = new PaginationServiceImpl(totalSize, Integer.parseInt(perPage), Integer.parseInt(page));
    ObjectMapper mapper = new ObjectMapper();
    JsonNode myObjectJson = mapper.valueToTree(new MetaResponseObjectDto(pagination.getCurrentPage(), pagination.getNextPage(), pagination.getPrevPage(), pagination.getTotalPages(), pagination.getTotalItems()));
    ObjectNode jsonObject = mapper.createObjectNode();
    jsonObject.set("pagination", myObjectJson);
    response.put("meta", jsonObject);
  }

  public String searchTextStartsWith(String searchText) {
    return "^" + searchText;
  }
  
  public String searchTextEndsWith(String searchText) {
    return searchText + "$" ;
  }
  
  public String searchTextContains(String searchText) {
    return "^" + searchText + "$" ;
  }

}

package com.fleetenable.billing.controllers.v2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fleetenable.billing.controllers.ApplicationController;
import com.fleetenable.billing.dtos.AccountAccessorialDto;
import com.fleetenable.billing.dtos.AccountAccessorialParamDto;
import com.fleetenable.billing.dtos.sub_entities.AccessorialParamsDto;
import com.fleetenable.billing.dtos.sub_entities.AccountAccessorialComponentParamsDto;
import com.fleetenable.billing.models.AccountAccessorial;
import com.fleetenable.billing.models.AccountAccessorialParam;
import com.fleetenable.billing.models.sub_entities.AccessorialWeightParams;
import com.fleetenable.billing.modules.AccountAccessorials;
import com.fleetenable.billing.repositories.AccountAccessorialParamRepository;
import com.fleetenable.billing.repositories.AccountAccessorialRepository;
import com.fleetenable.billing.services.CommonServices;

@RestController
public class AccountAccessorialsController extends ApplicationController{

  BigDecimal bd;

  @Autowired
  private CommonServices commonServices;

  @Autowired
  AccountAccessorialRepository accountAccessorialRepository;
  
  @Autowired
  AccountAccessorialParamRepository accountAccessorialParamRepository;

  @Autowired
  AccountAccessorials accountAccessorialsMod;

  @GetMapping("/v2/account_accessorials/account_accessorial_list")
  public ResponseEntity<Object> getAccountAccessorials(@RequestParam Map<String, String> requestParams ) {
    ArrayList<String> errors = new ArrayList<String>();
    Map<String, Object> response = new HashMap<String, Object>();

    String searchText = requestParams.get("search_text");
    String organizationId = requestParams.get("organization_id");
    String accountId = requestParams.get("account_id");
    // String visibleTo = requestParams.get("visible_to");
    String page = requestParams.get("page");
    String perPage = requestParams.get("per_page");

    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(perPage));

    if (errors.size() > 0) {
      response.put("errors", errors);
      return new ResponseEntity<Object>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    List<AccountAccessorial> accountAccessorials;

    if(searchText != null && !searchText.isEmpty()) {
      accountAccessorials = accountAccessorialRepository.findAccountIdAndOrganizationIdAndSearchText(accountId, organizationId, commonServices.searchTextStartsWith(searchText));
    } else {
      accountAccessorials = accountAccessorialRepository.findAccountIdAndOrganizationId(accountId, organizationId);
    }
    int accountAccessorialsSize = accountAccessorials.size();
    int start = (int)pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), accountAccessorialsSize);
    commonServices.paginationResponse(response, page, perPage, accountAccessorialsSize);
    response.put("account_accessorials", accountAccessorials.subList(start, end));

    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }

  @PostMapping("/v2/account_accessorail_params/create_account_accessorial")
  public ResponseEntity<Object> createAccountAccessorialParams(@RequestBody AccountAccessorialParamDto accountAccessorialParamDto) {
    ArrayList<String> errors = new ArrayList<String>();
    Map<String, Object> response = new HashMap<String, Object>();

    accountAccessorialsMod.validateCreateAccountAccessorialWeightParams(accountAccessorialParamDto, errors);

    if (errors.size() > 0) {
      response.put("errors", errors);
      return new ResponseEntity<Object>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    AccountAccessorial accountAccessorial = accountAccessorialRepository.findFirstByAccountIdAndOrganizationIdAndAccessorialCode(accountAccessorialParamDto.getAccount_id(), accountAccessorialParamDto.getOrganization_id(), accountAccessorialParamDto.getCode());
    if (accountAccessorial == null) {
      errors.add("Account accessorial no found");
      response.put("errors", errors);
      return new ResponseEntity<Object>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    accountAccessorial.setEnable(true);
    accountAccessorialRepository.save(accountAccessorial);

    List<AccountAccessorialComponentParamsDto> componentParams = accountAccessorialParamDto.getComponent_params();
    List<AccessorialWeightParams> accessorialWeightParams = new ArrayList<AccessorialWeightParams>();
    
    AccountAccessorialParam accountAccessorialParam = new AccountAccessorialParam();

    for (AccountAccessorialComponentParamsDto componentParam : componentParams) {
      List<AccessorialParamsDto> accessorialParams = componentParam.getAccessorial_params();
      String componentCode = componentParam.getComponent_code();
      String zoneCategory = componentParam.getZone_category() != null ? componentParam.getZone_category() : "ALL_ZONES";
      String accountAccessorialId = accountAccessorial.getId();

      accountAccessorialParam = accountAccessorialsMod._initiateAccountAccessorialParams(accountAccessorialParamDto, accountAccessorialId);
      accountAccessorialsMod.verifyBetweenRangeValues(errors, response, accountAccessorialParam, accessorialParams);
      if (errors.size() > 0) {
        response.put("errors", errors);
        return new ResponseEntity<Object>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }
      accountAccessorialsMod._saveAccountAccessorialParams(accessorialWeightParams, accountAccessorialParam, accessorialParams, componentCode, zoneCategory);
    }

    String withBreakpointWt = accountAccessorialsMod.getComponentWithBreakpointWtConfig(accountAccessorialParam);
    if (withBreakpointWt.equalsIgnoreCase("true")) {
      accountAccessorialParamRepository.save(accountAccessorialsMod.applyComponentBreakpointWeights(accountAccessorialParam));
    } else {
      accountAccessorialParamRepository.save(accountAccessorialsMod.clearComponentBreakpointWeights(accountAccessorialParam));
    }

    response.put("success", true);
    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }

  @PostMapping("/v2/account_accessorials/create_accessorial")
  public ResponseEntity<Object> createAccountAccessorial(@RequestBody AccountAccessorialDto accountAccessorialDto) {
    ArrayList<String> errors = new ArrayList<String>();
    Map<String, Object> response = new HashMap<String, Object>();

    accountAccessorialsMod.validateCreateAccountAccessorialParams(accountAccessorialDto, errors);

    if (errors.size() > 0) {
      response.put("errors", errors);
      return new ResponseEntity<Object>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    AccountAccessorial accountAccessorial;
    if(accountAccessorialDto.getId() == null || accountAccessorialDto.getId().isEmpty()) {
      accountAccessorial = accountAccessorialRepository.save(new AccountAccessorial(accountAccessorialDto.getAccount_id(), accountAccessorialDto.getOrganization_id(), accountAccessorialDto.getCode()));
    } else {
      accountAccessorial = accountAccessorialRepository.findById(accountAccessorialDto.getId()).get();
      accountAccessorial.setAccessorial_code(accountAccessorialDto.getCode());
    }
    accountAccessorialsMod.updateAccountAccessorialAttributes(accountAccessorialDto, accountAccessorial);

    accountAccessorialsMod.updateAccountAccessorialParams(accountAccessorial);
    response.put("message", "Successfully created!");
    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }
}

package com.fleetenable.billing.controllers.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fleetenable.billing.controllers.ApplicationController;
import com.fleetenable.billing.dtos.AccountAccessorialDto;
import com.fleetenable.billing.dtos.AccountAccessorialParamDto;
import com.fleetenable.billing.models.AccountAccessorial;
import com.fleetenable.billing.models.AccountAccessorialParam;
import com.fleetenable.billing.models.sub_entities.AccessorialWeightParams;
import com.fleetenable.billing.models.sub_entities.AccountAccessorialComponent;
import com.fleetenable.billing.repositories.AccountAccessorialParamRepository;
import com.fleetenable.billing.repositories.AccountAccessorialRepository;

@RestController
public class AccountAccessorialsController extends ApplicationController{

  @Autowired
  AccountAccessorialRepository accountAccessorialRepository;
  
  @Autowired
  AccountAccessorialParamRepository accountAccessorialParamRepository;

  @PostMapping("/v2/account_accessorail_params/create_account_accessorial")
  public ResponseEntity<Object> createAccountAccessorialParams(@RequestBody AccountAccessorialParamDto accountAccessorialParamDto) {
    ArrayList<String> errors = new ArrayList<String>();
    Map<String, Object> response = new HashMap<String, Object>();

    validateCreateAccountAccessorialParams(accountAccessorialParamDto, errors);

    if (errors.size() > 0) {
      response.put("errors", errors);
      return new ResponseEntity<Object>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }

  @PostMapping("/v2/account_accessorials/create_accessorial")
  public ResponseEntity<Object> createAccountAccessorial(@RequestBody AccountAccessorialDto accountAccessorialDto) {
    ArrayList<String> errors = new ArrayList<String>();
    Map<String, Object> response = new HashMap<String, Object>();

    validateCreateAccountAccessorialParams(accountAccessorialDto, errors);

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
    updateAccountAccessorialAttributes(accountAccessorialDto, accountAccessorial);

    updateAccountAccessorialParams(accountAccessorial);
    response.put("message", "Successfully created!");
    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }

  private void validateCreateAccountAccessorialParams(AccountAccessorialParamDto accountAccessorialParamDto, ArrayList<String> errors) {
    if(accountAccessorialParamDto.getAccount_id() == null || accountAccessorialParamDto.getAccount_id().isEmpty()){
      errors.add("Please provide account_id");
    }
    
    if(accountAccessorialParamDto.getOrganization_id() == null || accountAccessorialParamDto.getOrganization_id().isEmpty()){
      errors.add("Please provide organization_id");
    }
    
    if(accountAccessorialParamDto.getCode() == null || accountAccessorialParamDto.getCode().isEmpty()){
      errors.add("Please provide code");
    }
    
    if(accountAccessorialParamDto.getComponent_params() == null || accountAccessorialParamDto.getComponent_params().isEmpty()){
      errors.add("Please provide component_params");
    }
  }

  private void updateAccountAccessorialParams(AccountAccessorial accountAccessorial) {
    String accountAccessorialId = accountAccessorial.getId();
    String accountAccessorialCode = accountAccessorial.getAccessorial_code();
    List<AccountAccessorialComponent> accountAccessorialComponents = accountAccessorial.getComponents();
    Set<String> componentCodes = new HashSet<>();
    for (AccountAccessorialComponent accountAccessorialComponent : accountAccessorialComponents) {
      componentCodes.add(accountAccessorialComponent.getComponent_code());
    }
    AccountAccessorialParam accountAccessorialParam = accountAccessorialParamRepository.findFirstByAccessorialIdAndCode(accountAccessorialId, accountAccessorialCode);
    if(accountAccessorialParam != null) {
      List<AccessorialWeightParams> accessorialWeightParams = accountAccessorialParam.getAccessorial_weight_params();
      List<AccessorialWeightParams> newAccessorialWeightParams = new ArrayList<>();
      for (AccessorialWeightParams accessorialWeightParam : accessorialWeightParams) {
        if(componentCodes.contains(accessorialWeightParam.getComponent_code())) {
          newAccessorialWeightParams.add(accessorialWeightParam);
        }
      }
      accountAccessorialParam.setAccessorial_weight_params(newAccessorialWeightParams);
      accountAccessorialParamRepository.save(accountAccessorialParam);
    }
  }

  private void updateAccountAccessorialAttributes(AccountAccessorialDto accountAccessorialDto, AccountAccessorial accountAccessorial) {
    accountAccessorial.setAccessorial_name(accountAccessorialDto.getName());
    accountAccessorial.setAccessorial_code(accountAccessorialDto.getCode());
    accountAccessorial.setGl_code(accountAccessorialDto.getGl_code());
    accountAccessorial.setAccessible_to(accountAccessorialDto.getAccessible_to());
    accountAccessorial.setComponents(accountAccessorialDto.getComponents());
    accountAccessorial.setAccessorial_config_params(accountAccessorialDto.getAccessorial_config_params());
    accountAccessorialRepository.save(accountAccessorial);
  }

  private void validateCreateAccountAccessorialParams(AccountAccessorialDto accountAccessorialDto, ArrayList<String> errors) {
    if (accountAccessorialDto.getAccount_id().isEmpty()) {
      errors.add("Please provide account_id");
    }
    
    if (accountAccessorialDto.getOrganization_id().isEmpty()) {
      errors.add("Please provide organization_id");
    }
    
    if (accountAccessorialDto.getName().isEmpty()) {
      errors.add("Please provide name");
    }
    
    if (accountAccessorialDto.getCode().isEmpty()) {
      errors.add("Please provide code");
    }
  }
}

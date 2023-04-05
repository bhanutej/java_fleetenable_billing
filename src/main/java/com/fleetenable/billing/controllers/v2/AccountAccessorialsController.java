package com.fleetenable.billing.controllers.v2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.fleetenable.billing.dtos.sub_entities.WeightParamValuesDto;
import com.fleetenable.billing.models.AccountAccessorial;
import com.fleetenable.billing.models.AccountAccessorialParam;
import com.fleetenable.billing.models.sub_entities.AccessorialWeightParamValues;
import com.fleetenable.billing.models.sub_entities.AccessorialWeightParams;
import com.fleetenable.billing.models.sub_entities.AccountAccessorialComponent;
import com.fleetenable.billing.repositories.AccountAccessorialParamRepository;
import com.fleetenable.billing.repositories.AccountAccessorialRepository;
import com.fleetenable.billing.services.CommonServices;

@RestController
public class AccountAccessorialsController extends ApplicationController{

  BigDecimal bd;

  private DecimalFormat decimalFormat = new DecimalFormat("0.00");

  @Autowired
  private CommonServices commonServices;

  @Autowired
  AccountAccessorialRepository accountAccessorialRepository;
  
  @Autowired
  AccountAccessorialParamRepository accountAccessorialParamRepository;

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

    validateCreateAccountAccessorialWeightParams(accountAccessorialParamDto, errors);

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

      accountAccessorialParam = accountAccessorialParamRepository.findFirstByAccountAccessorialIdAndAccountId(accountAccessorialId, accountAccessorialParamDto.getAccount_id());
      if (accountAccessorialParam == null) {
        accountAccessorialParam = new AccountAccessorialParam(accountAccessorialParamDto.getAccount_id(), accountAccessorialId);
      }

      accountAccessorialParam.setOrganization_id(accountAccessorialParamDto.getOrganization_id());
      accountAccessorialParam.setCode(accountAccessorialParamDto.getCode());

      verifyBetweenRangeValues(errors, response, accountAccessorialParam, accessorialParams);

      if (errors.size() > 0) {
        response.put("errors", errors);
        return new ResponseEntity<Object>(response, HttpStatus.UNPROCESSABLE_ENTITY);
      }

      for(AccessorialParamsDto accessorialParam : accessorialParams) {
        AccessorialWeightParams newAccessorialWeightParams = updateWeightParamValues(accountAccessorialParam, componentCode,
            zoneCategory, accessorialParam);
        List<WeightParamValuesDto> weightParamValues = accessorialParam.getWt_param_values();
        
        List<AccessorialWeightParamValues> newWeightParamValues = new ArrayList<AccessorialWeightParamValues>();
        for(WeightParamValuesDto weightParamValue: weightParamValues) {
          AccessorialWeightParamValues newAccessorialWeightParamValues = new AccessorialWeightParamValues(weightParamValue.getAccessorial_key(), weightParamValue.getAccessorial_value(), weightParamValue.getZone_id());
          newWeightParamValues.add(newAccessorialWeightParamValues);
          newAccessorialWeightParams.setParam_values(newWeightParamValues);
        }
        accessorialWeightParams.add(newAccessorialWeightParams);
        accountAccessorialParam.setAccessorial_weight_params(accessorialWeightParams);
      }
      accountAccessorialParamRepository.save(accountAccessorialParam);
    }

    String withBreakpointWt = getComponentWithBreakpointWtConfig(accountAccessorialParam);
    if (withBreakpointWt.equalsIgnoreCase("true")) {
      accountAccessorialParamRepository.save(applyComponentBreakpointWeights(accountAccessorialParam));
    } else {
      accountAccessorialParamRepository.save(clearComponentBreakpointWeights(accountAccessorialParam));
    }

    response.put("success", true);
    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }

  private void verifyBetweenRangeValues(ArrayList<String> errors, Map<String, Object> response,
      AccountAccessorialParam accountAccessorialParam, List<AccessorialParamsDto> accessorialParams) {
    for (AccessorialParamsDto accessorialParam : accessorialParams) {
      if(accessorialParam.getParams_with_wt().equals("true")) {
        accountAccessorialParam.getAccessorial_weight_params().forEach(accessorialWtParam -> {
          float accessorialWtParamMinWt = Float.valueOf(decimalFormat.format(accessorialWtParam.getMin_weight()));
          float accessorialWtParamMaxWt = Float.valueOf(decimalFormat.format(accessorialWtParam.getMax_weight()));
          float accessorialParamMinWt  = Float.valueOf(Float.parseFloat(accessorialParam.getMin_weight()));
          float accessorialParamMaxWt  = Float.valueOf(Float.parseFloat(accessorialParam.getMax_weight()));
          if (((accessorialWtParamMinWt != accessorialParamMinWt) && (accessorialWtParamMaxWt != accessorialParamMaxWt)) &&
          ((accessorialWtParamMinWt <= accessorialParamMinWt && accessorialWtParamMaxWt >= accessorialParamMinWt) || 
          (accessorialWtParamMinWt <= accessorialParamMaxWt && accessorialWtParamMaxWt >= accessorialParamMaxWt))
          ){
            errors.add("Weights should not be with in previous weight range");
            response.put("errors", errors);
          }
        });
      }
    }
  }

  private AccessorialWeightParams updateWeightParamValues(AccountAccessorialParam accountAccessorialParam, String componentCode,
      String zoneCategory, AccessorialParamsDto accessorialParam) {
    AccessorialWeightParams newAccessorialWeightParams = new AccessorialWeightParams();
    accountAccessorialParam.setParams_with_wt(accessorialParam.getParams_with_wt());
    newAccessorialWeightParams.setMin_weight(Integer.parseInt(accessorialParam.getMin_weight()));
    newAccessorialWeightParams.setMax_weight(Integer.parseInt(accessorialParam.getMax_weight()));
    newAccessorialWeightParams.setComponent_code(componentCode);
    newAccessorialWeightParams.setOrder_type(accessorialParam.getOrder_type());
    newAccessorialWeightParams.setZone_category(zoneCategory);
    return newAccessorialWeightParams;
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

  private void validateCreateAccountAccessorialWeightParams(AccountAccessorialParamDto accountAccessorialParamDto, ArrayList<String> errors) {
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

  private AccountAccessorialParam  applyComponentBreakpointWeights(AccountAccessorialParam accountAccessorialParam) {
    if (accountAccessorialParam != null) {
      List<AccessorialWeightParams> newAccessorialWeightParams = new ArrayList<AccessorialWeightParams>();
      List<AccessorialWeightParams> accessorialWeightParams = accountAccessorialParam.getAccessorial_weight_params();
      List<AccessorialWeightParams> weightedAccessorialWeightParams = new ArrayList<AccessorialWeightParams>();
      for(AccessorialWeightParams accessorialWeightParam: accessorialWeightParams) {
        if (accessorialWeightParam.getMax_weight() != 0) {
          weightedAccessorialWeightParams.add(accessorialWeightParam);
        }
      }
      weightedAccessorialWeightParams = sortAccessorialWeightParams(weightedAccessorialWeightParams);
      for(int i = 0; i < weightedAccessorialWeightParams.size(); i++) {
        AccessorialWeightParams currentWeightParam = weightedAccessorialWeightParams.get(i);
        if (i+1 < weightedAccessorialWeightParams.size()) {
          AccessorialWeightParams nextWeightParam = weightedAccessorialWeightParams.get(i+1);
          if(nextWeightParam != null) {
            int nextWtParamMinWt = nextWeightParam.getMin_weight();
            float nextWtParamAmount = Float.parseFloat( nextWeightParam.getParam_values().get(0).getAccessorial_value() );
            float currentWtParamAmount = Float.parseFloat( currentWeightParam.getParam_values().get(0).getAccessorial_value() );
            BigDecimal breakPointWeight = new BigDecimal((nextWtParamMinWt * nextWtParamAmount)/(currentWtParamAmount));
            breakPointWeight = breakPointWeight.setScale(2, RoundingMode.HALF_UP);
            currentWeightParam.setBreakpoint_weight((float) breakPointWeight.floatValue());
          }
        }
        newAccessorialWeightParams.add(currentWeightParam);
      }
      for(AccessorialWeightParams accessorialWeightParam: accessorialWeightParams) {
        if (accessorialWeightParam.getMax_weight() == 0) {
          newAccessorialWeightParams.add(accessorialWeightParam);
        }
      }
      accountAccessorialParam.setAccessorial_weight_params(newAccessorialWeightParams);
    }
    return accountAccessorialParam;
  }

  private AccountAccessorialParam  clearComponentBreakpointWeights(AccountAccessorialParam accountAccessorialParam) {
    List<AccessorialWeightParams> accessorialWeightParams = accountAccessorialParam.getAccessorial_weight_params();
    accessorialWeightParams.removeIf(accWtParam -> accWtParam.getBreakpoint_weight() != null);
    return accountAccessorialParam;
  }

  public static List<AccessorialWeightParams> sortAccessorialWeightParams(List<AccessorialWeightParams> weightedAccessorialWeightParams) {
    int n = weightedAccessorialWeightParams.size();
    AccessorialWeightParams temp;
    for(int i = 0; i < n; i++) {
      for(int j = 1; j < (n - i); j++) {
        if(weightedAccessorialWeightParams.get(j-1).getMin_weight() > weightedAccessorialWeightParams.get(j).getMin_weight()) {
          temp = weightedAccessorialWeightParams.get(j-1);
          weightedAccessorialWeightParams.set(j-1, weightedAccessorialWeightParams.get(j));
          weightedAccessorialWeightParams.set(j, temp);
        }
      }
    }
    return weightedAccessorialWeightParams;
  }

  private String getComponentWithBreakpointWtConfig(AccountAccessorialParam accountAccessorialParam) {
    String withBreakpointWt = "false";
    List<AccessorialWeightParams> accessorialWeightParams = accountAccessorialParam.getAccessorial_weight_params();
    if (accessorialWeightParams.size() > 0) {
      for(AccessorialWeightParams accessorialWeightParam: accessorialWeightParams) {
        if (accessorialWeightParam.getMax_weight() == 0 && accessorialWeightParam.getMax_weight() == 0 && accessorialWeightParam.getComponent_code().equalsIgnoreCase("WEIGHT")) {
          AccessorialWeightParams zeroWeightAccessorialParam = accessorialWeightParam;
          if (zeroWeightAccessorialParam.getParam_values().size() > 0) {
            List<AccessorialWeightParamValues> accessorialWeightParamValues = zeroWeightAccessorialParam.getParam_values();
            for(AccessorialWeightParamValues accessorialWeightParamValue: accessorialWeightParamValues) {
              if (accessorialWeightParamValue.getAccessorial_key().equalsIgnoreCase("with_break_wt")) {
                withBreakpointWt = accessorialWeightParamValue.getAccessorial_value();
              }
            }
          }
        }
      }
    }
    return withBreakpointWt;
  }
}

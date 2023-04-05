package com.fleetenable.billing.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fleetenable.billing.dtos.AccountAccessorialDto;
import com.fleetenable.billing.dtos.AccountAccessorialParamDto;
import com.fleetenable.billing.dtos.sub_entities.AccessorialParamsDto;
import com.fleetenable.billing.dtos.sub_entities.WeightParamValuesDto;
import com.fleetenable.billing.models.AccountAccessorial;
import com.fleetenable.billing.models.AccountAccessorialParam;
import com.fleetenable.billing.models.sub_entities.AccessorialWeightParamValues;
import com.fleetenable.billing.models.sub_entities.AccessorialWeightParams;
import com.fleetenable.billing.models.sub_entities.AccountAccessorialComponent;
import com.fleetenable.billing.repositories.AccountAccessorialParamRepository;
import com.fleetenable.billing.repositories.AccountAccessorialRepository;

@Component
public class AccountAccessorials {

  BigDecimal bd;

  public DecimalFormat decimalFormat = new DecimalFormat("0.00");

  @Autowired
  AccountAccessorialParamRepository accountAccessorialParamRepository;
  
  @Autowired
  AccountAccessorialRepository accountAccessorialRepository;

  public void _saveAccountAccessorialParams(List<AccessorialWeightParams> accessorialWeightParams,
      AccountAccessorialParam accountAccessorialParam, List<AccessorialParamsDto> accessorialParams,
      String componentCode, String zoneCategory) {
    for(AccessorialParamsDto accessorialParam : accessorialParams) {
      _updateAccountAccessorialWtParams(accessorialWeightParams, accountAccessorialParam, componentCode, zoneCategory, accessorialParam);
    }
    accountAccessorialParamRepository.save(accountAccessorialParam);
  }

  public AccountAccessorialParam _initiateAccountAccessorialParams(AccountAccessorialParamDto accountAccessorialParamDto,
      String accountAccessorialId) {
    AccountAccessorialParam accountAccessorialParam;
    accountAccessorialParam = accountAccessorialParamRepository.findFirstByAccountAccessorialIdAndAccountId(accountAccessorialId, accountAccessorialParamDto.getAccount_id());
    if (accountAccessorialParam == null) {
      accountAccessorialParam = new AccountAccessorialParam(accountAccessorialParamDto.getAccount_id(), accountAccessorialId);
    }

    accountAccessorialParam.setOrganization_id(accountAccessorialParamDto.getOrganization_id());
    accountAccessorialParam.setCode(accountAccessorialParamDto.getCode());
    return accountAccessorialParam;
  }

  public void _updateAccountAccessorialWtParams(List<AccessorialWeightParams> accessorialWeightParams,
      AccountAccessorialParam accountAccessorialParam, String componentCode, String zoneCategory,
      AccessorialParamsDto accessorialParam) {
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

  public void verifyBetweenRangeValues(ArrayList<String> errors, Map<String, Object> response,
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

  public AccessorialWeightParams updateWeightParamValues(AccountAccessorialParam accountAccessorialParam, String componentCode,
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

  public void validateCreateAccountAccessorialWeightParams(AccountAccessorialParamDto accountAccessorialParamDto, ArrayList<String> errors) {
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

  public void updateAccountAccessorialParams(AccountAccessorial accountAccessorial) {
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

  public void updateAccountAccessorialAttributes(AccountAccessorialDto accountAccessorialDto, AccountAccessorial accountAccessorial) {
    accountAccessorial.setAccessorial_name(accountAccessorialDto.getName());
    accountAccessorial.setAccessorial_code(accountAccessorialDto.getCode());
    accountAccessorial.setGl_code(accountAccessorialDto.getGl_code());
    accountAccessorial.setAccessible_to(accountAccessorialDto.getAccessible_to());
    accountAccessorial.setComponents(accountAccessorialDto.getComponents());
    accountAccessorial.setAccessorial_config_params(accountAccessorialDto.getAccessorial_config_params());
    accountAccessorialRepository.save(accountAccessorial);
  }

  public void validateCreateAccountAccessorialParams(AccountAccessorialDto accountAccessorialDto, ArrayList<String> errors) {
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

  public AccountAccessorialParam  applyComponentBreakpointWeights(AccountAccessorialParam accountAccessorialParam) {
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

  public AccountAccessorialParam  clearComponentBreakpointWeights(AccountAccessorialParam accountAccessorialParam) {
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

  public String getComponentWithBreakpointWtConfig(AccountAccessorialParam accountAccessorialParam) {
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

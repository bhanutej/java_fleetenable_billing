package com.fleetenable.billing.controllers.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fleetenable.billing.controllers.ApplicationController;
import com.fleetenable.billing.dtos.BillingZoneRequestDto;
import com.fleetenable.billing.dtos.BillingZoneDto;
import com.fleetenable.billing.models.Account;
import com.fleetenable.billing.models.BillingZone;
import com.fleetenable.billing.modules.BillingZonesMod;
import com.fleetenable.billing.repositories.AccountRepository;
import com.fleetenable.billing.repositories.BillingRateRepository;
import com.fleetenable.billing.repositories.BillingZoneRepository;

@RestController
public class BillingZonesController extends ApplicationController{

  @Autowired
  private BillingZoneRepository billingZoneRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private BillingRateRepository billingRateRepository;

  @Autowired
  private BillingZonesMod billingZonesMod;
  
  @GetMapping("/v2/billing_zones")
  public ResponseEntity<Object> getBillingZones(@RequestParam String account_id) {
    Sort sortZones = Sort.by(Sort.Direction.ASC, "min_distance");
    List<BillingZone> billingZones = billingZoneRepository.findByAccountId(account_id, sortZones);
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("billing_zones", billingZones);
    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }

  @DeleteMapping("/v2/billing_zones/delete_zone")
  public ResponseEntity<Object> deleteBillinZone(@RequestParam String id) {
    ArrayList<String> errors = new ArrayList<String>();
    Map<String, Object> response = new HashMap<String, Object>();

    Optional<BillingZone> billingZone = billingZoneRepository.findById(id);
    if (billingZone == null || billingZone.isEmpty()) {
      errors.add("Billing Zone not found");
      response.put("errors", errors);
      return new ResponseEntity<Object>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    billingRateRepository.deleteByZoneId(id);
    billingZoneRepository.delete(billingZone.get());
    response.put("message", "Zone deleted successfully!");
    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }

  @PostMapping("/v2/billing_zones")
  public ResponseEntity<Object> createBillingZones(@RequestBody BillingZoneRequestDto billingZoneRequestDto) {
    ArrayList<String> errors = new ArrayList<String>();
    Map<String, Object> response = new HashMap<String, Object>();

    String zoneType = billingZoneRequestDto.getZone_type();
    String accountId = billingZoneRequestDto.getAccount_id();

    if (zoneType.equals("distance")) {
      billingZonesMod.validateZoneDistances(billingZoneRequestDto, errors);
    }

    if (errors.size() > 0) {
      response.put("errors", errors);
      return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    List<BillingZoneDto> zones = billingZoneRequestDto.getZones();
    for(BillingZoneDto zone : zones) {
      Account account = accountRepository.findByAccountId(accountId);
      String organizationId = account.getOrganization_id();

      if (zone.getId() != null) {
        BillingZone billingZone = billingZoneRepository.findByZoneId(zone.getId());
        if (billingZone != null) {
          billingZone.setName(zone.getName());
          billingZonesMod.updateBillinZones(zoneType, zone.getZip_codes(), zone.getMin_distance(), zone.getMax_distance(), billingZone);
          billingZoneRepository.save(billingZone);
        }
      } else {
        BillingZone billingZone = billingZoneRepository.save(new BillingZone(zone.getName(), zoneType, accountId, organizationId));
        if (billingZone != null) {
          billingZonesMod.updateBillinZones(zoneType, zone.getZip_codes(), zone.getMin_distance(), zone.getMax_distance(), billingZone);
          BillingZone newBillingZone = billingZoneRepository.save(billingZone);
          if (newBillingZone != null) {
            billingZonesMod.updateBillingRateWithNewZone(accountId, organizationId, newBillingZone);
          }
        }
      }
    }

    response.put("message", "Successfully created!");
    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }
}

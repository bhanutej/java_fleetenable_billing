package com.fleetenable.billing.controllers.v2;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.fleetenable.billing.models.BillingRate;
import com.fleetenable.billing.models.BillingZone;
import com.fleetenable.billing.repositories.AccountRepository;
import com.fleetenable.billing.repositories.BillingRateRepository;
import com.fleetenable.billing.repositories.BillingZoneRepository;

@RestController
public class BillingZonesController extends ApplicationController{

  private DecimalFormat decimalFormat = new DecimalFormat("0.00");

  @Autowired
  private BillingZoneRepository billingZoneRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private BillingRateRepository billingRateRepository;
  
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
      validateZoneDistances(billingZoneRequestDto, errors);
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
          updateBillinZones(zoneType, zone.getZip_codes(), zone.getMin_distance(), zone.getMax_distance(), billingZone);
          billingZoneRepository.save(billingZone);
        }
      } else {
        BillingZone billingZone = billingZoneRepository.save(new BillingZone(zone.getName(), zoneType, accountId, organizationId));
        if (billingZone != null) {
          updateBillinZones(zoneType, zone.getZip_codes(), zone.getMin_distance(), zone.getMax_distance(), billingZone);
          BillingZone newBillingZone = billingZoneRepository.save(billingZone);
          if (newBillingZone != null) {
            updateBillingRateWithNewZone(accountId, organizationId, newBillingZone);
          }
        }
      }
    }

    response.put("message", "Successfully created!");
    return new ResponseEntity<Object>(response, HttpStatus.OK);
  }

  private void updateBillinZones(String zoneType, List<String> zipcodes, Double minDistance, Double maxDistance,
      BillingZone billingZone) {
    if (zoneType.equals("distance")) {
      billingZone.setMin_distance(minDistance);
      billingZone.setMax_distance(maxDistance);
    } else {
      billingZone.setZip_codes(zipcodes);
      ArrayList<String> listZipCodeWithoutDuplicateRanges = getGeneratedZipcodes(zipcodes);
      billingZone.setZip_codes_to_compare(listZipCodeWithoutDuplicateRanges);
    }
  }

  private void updateBillingRateWithNewZone(String accountId, String organizationId, BillingZone newBillingZone) {
    List<BillingRate> billingRates = billingRateRepository.searchByAccountId(accountId);
    for(BillingRate billingRate: billingRates) {
      float minWeight = billingRate.getMin_weight();
      float maxWeight = billingRate.getMax_weight();
      String billinZoneId = newBillingZone.getId();
      BillingRate isBillingRateExisted = billingRateRepository.isBillingRateExisted(accountId, billinZoneId, minWeight, maxWeight);
      if (isBillingRateExisted == null) {
        billingRateRepository.save(new BillingRate(accountId, organizationId, billinZoneId, minWeight, maxWeight));
      }
    }
  }

  private ArrayList<String> getGeneratedZipcodes(List<String> zipcodes) {
    ArrayList<String> zipCodewithDuplicateRanges = new ArrayList<String>();
    for(String zipcode: zipcodes) {
      if (zipcode.length() == 5) {
        zipCodewithDuplicateRanges.add(zipcode);
      } else if (zipcode.length() == 4) {
        for(int i = 0; i < 10; i++) {
          zipCodewithDuplicateRanges.add(zipcode + i);
        }
      } else if (zipcode.length() == 3) {
        for(int i = 0; i < 100; i++) {
          zipCodewithDuplicateRanges.add(zipcode + i);
        }
      }
    }
    HashSet<String> zipCodewithoutDuplicateRanges = new HashSet<>(zipCodewithDuplicateRanges);
    ArrayList<String> listZipCodeWithoutDuplicateRanges = new ArrayList<>(zipCodewithoutDuplicateRanges);
    return listZipCodeWithoutDuplicateRanges;
  }

  private void validateZoneDistances(BillingZoneRequestDto billingZoneRequestDto, ArrayList<String> errors) {
    List<BillingZoneDto> zones = billingZoneRequestDto.getZones();
    if (zones.size() > 0) {
      List<BillingZoneDto> sortedZones = zones.stream()
        .sorted(Comparator.comparing(BillingZoneDto::getMin_distance))
        .collect(Collectors.toList());
      BillingZoneDto[] sortedZonesArr
        = sortedZones.stream().toArray(BillingZoneDto[] ::new);
      for(int i = 0; i < sortedZonesArr.length; i++ ){
        if (i > 0) {
          BillingZoneDto prevZone = (BillingZoneDto) Array.get(sortedZonesArr, i - 1);
          BillingZoneDto currentZone = (BillingZoneDto) Array.get(sortedZonesArr, i);
          String currentZoneName = currentZone.getName();
          String prevZoneName = prevZone.getName();
          double prevZoneMaxDistance = prevZone.getMax_distance();
          double currentZoneMinDistance = currentZone.getMin_distance();
          if (Double.valueOf(decimalFormat.format(currentZoneMinDistance - prevZoneMaxDistance)) != 0.01d ) {
            String errorString = String.format("%s's minimum distance should be next to the %s's maximum distance ", currentZoneName, prevZoneName);
            errors.add(errorString);
          }
        }
      }
    }
  }
}

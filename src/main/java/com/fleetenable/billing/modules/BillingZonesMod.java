package com.fleetenable.billing.modules;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fleetenable.billing.dtos.BillingZoneDto;
import com.fleetenable.billing.dtos.BillingZoneRequestDto;
import com.fleetenable.billing.models.BillingRate;
import com.fleetenable.billing.models.BillingZone;
import com.fleetenable.billing.repositories.BillingRateRepository;

@Component
public class BillingZonesMod {

  private DecimalFormat decimalFormat = new DecimalFormat("0.00");
  
  @Autowired
  BillingRateRepository billingRateRepository;

  public void updateBillinZones(String zoneType, List<String> zipcodes, Double minDistance, Double maxDistance,
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

  public void updateBillingRateWithNewZone(String accountId, String organizationId, BillingZone newBillingZone) {
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

  public ArrayList<String> getGeneratedZipcodes(List<String> zipcodes) {
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

  public void validateZoneDistances(BillingZoneRequestDto billingZoneRequestDto, ArrayList<String> errors) {
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

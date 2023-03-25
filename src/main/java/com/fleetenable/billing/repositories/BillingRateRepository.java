package com.fleetenable.billing.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fleetenable.billing.models.BillingRate;

public interface BillingRateRepository extends MongoRepository<BillingRate, String> {
  
  @Query("{'zone_id': ?0}")
  void deleteByZoneId(String zone_id);

  @Query("{'account_id': ?0}")
  List<BillingRate> searchByAccountId(String account_id);
  
  @Query("{'account_id': ?0, 'zone_id': ?1, 'min_weight': ?2, 'max_weight': ?3}")
  BillingRate isBillingRateExisted(String account_id, String zone_id, Float min_weight, Float max_weight);
}

package com.fleetenable.billing.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fleetenable.billing.models.BillingZone;

@Repository
public interface BillingZoneRepository extends MongoRepository<BillingZone, String>{
  
  @Query("{'account_id': ?0}")
  List<BillingZone> findByAccountId(String account_id, Sort sortZones);
  
  @Query("{'account_id': ?0}")
  Long deleteByAccountId(String account_id);
  
  @Query("{'id': ?0}")
  BillingZone findByZoneId(String zone_id);

  void delete(BillingZone billingZone);
}

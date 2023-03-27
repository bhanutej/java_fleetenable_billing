package com.fleetenable.billing.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fleetenable.billing.models.AccountAccessorial;

public interface AccountAccessorialRepository extends MongoRepository<AccountAccessorial, String>{

  @Query("{'account_id': ?0, 'organization_id': ?1, 'accessorial_code': ?2}")
  AccountAccessorial findFirstByAccountIdAndOrganizationIdAndAccessorialCode(String account_id, String organization_id, String accessorial_code);
}

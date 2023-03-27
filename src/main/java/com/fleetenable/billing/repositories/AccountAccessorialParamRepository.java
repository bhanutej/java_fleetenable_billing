package com.fleetenable.billing.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fleetenable.billing.models.AccountAccessorialParam;

public interface AccountAccessorialParamRepository extends MongoRepository<AccountAccessorialParam, String>{
  @Query("{'account_accessorial_id': ?0, 'code': ?1}")
  AccountAccessorialParam findFirstByAccessorialIdAndCode(String account_accessorial_id, String code);
  
  @Query("{'account_accessorial_id': ?0, 'account_id': ?1}")
  AccountAccessorialParam findFirstByAccountAccessorialIdAndAccountId(String account_accessorial_id, String account_id);
}

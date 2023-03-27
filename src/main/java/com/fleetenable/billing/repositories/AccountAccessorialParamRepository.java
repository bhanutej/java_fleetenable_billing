package com.fleetenable.billing.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fleetenable.billing.models.AccountAccessorialParam;

public interface AccountAccessorialParamRepository extends MongoRepository<AccountAccessorialParam, String>{
  @Query("{'account_accessorial_id': ?0, 'code': ?1}")
  AccountAccessorialParam findFirstByAccessorialIdAndCode(String account_accessorial_id, String code);
  
  @Query("{'account_accessorial_id': ?0, 'account_id': ?1}")
  AccountAccessorialParam findFirstByAccountAccessorialIdAndAccountId(String account_accessorial_id, String account_id);

  @Query("{'account_accessorial_id': ?0, 'account_id': ?1, 'code': ?2, params_with_wt: ?3}")
  AccountAccessorialParam findFirstByAccountAccessorialIdAndAccountIdAndCodeAndParamsWithWt(String account_accessorial_id, String account_id, String code, String params_with_wt);
}

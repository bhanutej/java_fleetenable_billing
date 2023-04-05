package com.fleetenable.billing.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fleetenable.billing.models.AccountAccessorial;

@Repository
public interface AccountAccessorialRepository extends MongoRepository<AccountAccessorial, String>{

  @Query("{'account_id': ?0, 'organization_id': ?1, 'accessorial_code': ?2}")
  AccountAccessorial findFirstByAccountIdAndOrganizationIdAndAccessorialCode(String account_id, String organization_id, String accessorial_code);

  @Query("{'account_id': ?0, 'organization_id': ?1, $or:[{'accessorial_code': { $regex: ?2, $options: 'i' }}, {'accessorial_name': { $regex: ?2, $options: 'i' }}]}")
  List<AccountAccessorial> findAccountIdAndOrganizationIdAndSearchText(String account_id, String organization_id, String searchText);
  
  @Query("{'account_id': ?0, 'organization_id': ?1}")
  List<AccountAccessorial> findAccountIdAndOrganizationId(String account_id, String organization_id);
}

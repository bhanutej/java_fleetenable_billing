package com.fleetenable.billing.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fleetenable.billing.models.AccountAccessorial;

public interface AccountAccessorialRepository extends MongoRepository<AccountAccessorial, String>{
  
}

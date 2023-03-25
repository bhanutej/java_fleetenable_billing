package com.fleetenable.billing.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fleetenable.billing.models.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String>{
  @Query("{'id': ?0}")
  Account findByAccountId(String account_id);
}

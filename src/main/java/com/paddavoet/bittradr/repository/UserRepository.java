package com.paddavoet.bittradr.repository;

import com.paddavoet.bittradr.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
//    TradeVelocity findTradeVelocitiesByDirection(TradeValueDirection direction);
}

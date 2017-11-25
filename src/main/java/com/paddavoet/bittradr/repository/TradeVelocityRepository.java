package com.paddavoet.bittradr.repository;

import com.paddavoet.bittradr.entity.TradeVelocity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeVelocityRepository extends MongoRepository<TradeVelocity, Integer> {
//    TradeVelocity findTradeVelocitiesByDirection(TradeValueDirection direction);
}

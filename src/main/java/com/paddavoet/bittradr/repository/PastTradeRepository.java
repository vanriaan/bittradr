package com.paddavoet.bittradr.repository;

import com.paddavoet.bittradr.entity.PastTradeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PastTradeRepository extends MongoRepository<PastTradeEntity, Integer> {
//    TradeVelocity findTradeVelocitiesByDirection(TradeValueDirection direction);
}

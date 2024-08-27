package com.ipinformation.services;

import com.ipinformation.model.responses.IpLookupResponse;
import com.ipinformation.model.responses.StatsResponse;
import com.mongodb.client.model.Accumulators;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.mongodb.client.model.Aggregates.*;

@Service
public class DbService {

    private final MongoTemplate mongoDb;


    public DbService(MongoTemplate mongoDb) {
        this.mongoDb = mongoDb;
    }

    @Async
    protected void saveToDbAsync(Object response) {
        mongoDb.save(response);
    }

    public StatsResponse getStats() {

        String collectionName = mongoDb.getCollectionName(IpLookupResponse.class);

        Bson maxMinDistanceAggregation = group(null, Accumulators.max("maxDistance", "$distanceToBA"), Accumulators.min("minDistance", "$distanceToBA"), Accumulators.avg("avgDistance", "$distanceToBA"));


        return mongoDb.getCollection(collectionName).aggregate(List.of(maxMinDistanceAggregation), StatsResponse.class).first();
    }


}

package com.ipinformation.services;

import com.ipinformation.model.responses.IpLookupResponse;
import com.ipinformation.model.responses.Response;
import com.ipinformation.model.responses.StatsResponse;
import com.mongodb.client.model.Accumulators;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Locale;

import static com.mongodb.client.model.Aggregates.*;

@Service
@Slf4j
public class DbService {

    private final MongoTemplate mongoDb;
    private final MessageSource messageSource;


    public DbService(MongoTemplate mongoDb, MessageSource messageSource) {
        this.mongoDb = mongoDb;
        this.messageSource = messageSource;
    }

//    This method doesn't send an exception to the caller, it just logs it. So if the database loses connection when saving, the user won't know about it.
    @Async
    protected void saveToDbAsync(Object response) {
        mongoDb.save(response);
    }

    protected <T extends Response> T getFromDb(String id, Class<T> classInstance) {
        try {
            return mongoDb.findById(id, classInstance);
        } catch (DataAccessResourceFailureException e) {
            log.error(messageSource.getMessage("db.read.error", null, Locale.getDefault()), e);
            return null;
        }
    }

    public StatsResponse getStats() {

        String collectionName = mongoDb.getCollectionName(IpLookupResponse.class);

        Bson distanceAggregationStats = group(null, Accumulators.max("maxDistance", "$distanceToBA"), Accumulators.min("minDistance", "$distanceToBA"), Accumulators.avg("avgDistance", "$distanceToBA"));


        return mongoDb.getCollection(collectionName).aggregate(List.of(distanceAggregationStats), StatsResponse.class).first();
    }


}

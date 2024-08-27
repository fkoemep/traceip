package com.ipinformation.services;

import com.ipinformation.model.responses.StatsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StatisticsController {

    private final DbService dbService;

    public StatisticsController(DbService dbService) {
        this.dbService = dbService;
    }

    @RequestMapping(value = "/stats", method = { RequestMethod.POST,  RequestMethod.GET })
    public ResponseEntity<Object> getStats() {

        StatsResponse response = dbService.getStats();

        if (response == null || response.getMaxDistance() == null) {
            return new ResponseEntity<>("Service has not been utilized yet, please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}

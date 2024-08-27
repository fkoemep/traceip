package com.ipinformation.services;

import com.ipinformation.model.responses.IpLookupResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.validator.routines.InetAddressValidator;

@Controller
public class LookupController {

    private final DbService dbService;

    private final RequestService requestService;


    public LookupController(DbService dbService, RequestService requestService) {
        this.dbService = dbService;
        this.requestService = requestService;
    }


    @GetMapping("/lookup")
    public String lookupForm(Model model) {
        return "lookup";
    }


    @PostMapping("/lookup")
    public String lookupSubmit(Model model, @RequestParam String ipAddress) {

        InetAddressValidator validator = InetAddressValidator.getInstance();

        if(!validator.isValid(ipAddress)){
            model.addAttribute("errorMessage","Invalid IP address");
            return "lookup";
        }

        IpLookupResponse response = requestService.getResponse(ipAddress);

        SortedMap<String, LocalDateTime> timeMap = new TreeMap<>();

        response.getTimeZones().forEach(timeZone -> {
            LocalDateTime nowAtUtc = LocalDateTime.now(ZoneOffset.UTC);
            ZonedDateTime zoned = nowAtUtc.atZone(ZoneOffset.of(timeZone));
            timeMap.put(timeZone, zoned.toLocalDateTime());
        });

        response.setLocalTimes(timeMap);

        model.addAttribute("result", response);

        dbService.saveToDbAsync(response);

        return "lookupResult";
    }

}
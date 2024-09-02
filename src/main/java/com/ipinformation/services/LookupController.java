package com.ipinformation.services;

import com.ipinformation.exceptions.ApiErrorException;
import com.ipinformation.model.responses.IpLookupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Controller
public class LookupController {

    private final DbService dbService;

    private final RequestService requestService;

    private final MessageSource messageSource;

    public LookupController(DbService dbService, RequestService requestService, MessageSource messageSource) {
        this.dbService = dbService;
        this.requestService = requestService;
        this.messageSource = messageSource;
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
        IpLookupResponse response;

        try {
            response = requestService.getResponse(ipAddress);
        }
        catch (ApiErrorException e){
            model.addAttribute("errorMessage", messageSource.getMessage("error.internal", null, Locale.getDefault()));
            return "lookupResult";
        }
        catch (HttpClientErrorException e){
            model.addAttribute("errorMessage", messageSource.getMessage("error.internal", null, Locale.getDefault()));
            log.error(messageSource.getMessage("error.external.api.http.codes", new Object[]{e.getStatusCode().value(), e.getLocalizedMessage()}, Locale.getDefault()), e);
            return "lookupResult";
        }
        catch (ResourceAccessException e){
            model.addAttribute("errorMessage", messageSource.getMessage("error.internal", null, Locale.getDefault()));
            log.error(messageSource.getMessage("error.external.api.unreachable", null, Locale.getDefault()), e);

            return "lookupResult";
        }

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
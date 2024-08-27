package com.ipinformation.model.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.ipinformation.model.objects.Currency;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class CurrenciesDeserializer extends StdDeserializer<List<Currency>> {

    public CurrenciesDeserializer(Class<?> vc) {
        super(vc);
    }

    public CurrenciesDeserializer() {
        this(null);
    }

    @Override
    public List<Currency> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        List<Currency> result = new ArrayList<>();

        node.fields().forEachRemaining(entry -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Currency currency = mapper.treeToValue(entry.getValue(), Currency.class);
                currency.setCode(entry.getKey());
                result.add(currency);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


        });

        return result;
    }
}

class TimeZonesDeserializer extends StdDeserializer<List<String>> {

    public TimeZonesDeserializer(Class<?> vc) {
        super(vc);
    }

    public TimeZonesDeserializer() {
        this(null);
    }

    @Override
    public List<String> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        List<String> result = new ArrayList<>();

        node.forEach(entry -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String originalString = mapper.treeToValue(entry, String.class);

                String parsed = originalString.replace("UTC", originalString.equals("UTC") ? "+00:00" : "");
                result.add(parsed);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        });

        return result;
    }
}


@Document("DevMeCountryApiResponse")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DevMeCountryApiResponse implements Serializable {

    public DevMeCountryApiResponse() {

    }

    @Id
    private String code;

    @JsonDeserialize(using = CurrenciesDeserializer.class)
    private List<Currency> currencies;
    private ArrayList<Integer> latlng;
    @JsonDeserialize(using = TimeZonesDeserializer.class)
    private ArrayList<String> timezones;

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public Currency getFirstCurrency() {
        return currencies.getFirst();
    }

    public ArrayList<Integer> getLatlng() {
        return latlng;
    }

    public Integer getLatitude() {
        return latlng.getFirst();
    }

    public Integer getLongitude() {
        return latlng.getLast();
    }

    public ArrayList<String> getTimezones() {
        return timezones;
    }

    public String getCode() {
        return code;
    }
}

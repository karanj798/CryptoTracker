package com.assignment.utils;

import com.assignment.models.Currency;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class utilizes Gson serialization library to
 * convert JSON payload to Java objects.
 *
 * @author Karan
 * @version v1.0
 */
public class Serialize {
    private final Gson gson = new Gson();
    private final String content;
    private List<Currency> currencies = new ArrayList<>();

    /**
     * Initializes {@code content} with user specified parameter.
     *
     * @param content {@code JSON} payload
     */
    public Serialize(String content) {
        this.content = content;
    }

    /**
     * This method iterates over {@code JSONArray}, creates {@code Currency} objects
     * and stores them in a {@code List<Currency>}.
     */
    public void parseCurrencies() {
        JSONArray jsonArray = new JSONArray(this.content);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject localJSONObject = (JSONObject) jsonArray.get(i);
            Currency c = this.gson.fromJson(localJSONObject.toString(), Currency.class);
            currencies.add(c);
        }
    }

    /**
     * This method filters all of the currency by checking if they are crypto-currency.
     *
     * @return the list of filtered {@code Currency} objects.
     */
    public List<Currency> parseCryptoCurrencies() {
        parseCurrencies();
        return currencies.stream().filter(currency -> currency.isCrypto()).collect(Collectors.toList());
    }

    /**
     * This method gets the {@code JSONArray} and returns it.
     *
     * @return {@code JSONArray}.
     */
    public String parseDataPoints() {
        JSONObject jsonObject = new JSONObject(this.content).getJSONObject("data");
        JSONArray jsonArray = jsonObject.getJSONArray("prices");
        return jsonArray.toString();
    }
}

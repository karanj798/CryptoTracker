package com.assignment.utils;

import com.assignment.models.Currency;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
     * Returns the currency {@code List<Currency>}.
     * @return currency {@code List<Currency>}
     */
    public List<Currency> getCurrencies() {
        return currencies;
    }
}

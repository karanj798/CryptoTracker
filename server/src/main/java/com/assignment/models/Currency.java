package com.assignment.models;

import com.google.gson.annotations.SerializedName;

/**
 * This class is used for serialization.
 *
 * @author Karan
 * @version v1.0
 */
public class Currency {

    public String id;
    public String name;

    @SerializedName("min_size")
    public String minSize;
    public String status;
    public String message;
    public CurrencyDetails details;

    @SerializedName("max_precision")
    public String maxPrecision;

    /**
     * Initializes all the instance variables.
     *
     * @param id id of the currency.
     * @param name name of the currency.
     * @param minSize minimum size of currency.
     * @param status status of currency.
     * @param message message of currency.
     * @param details detail of currency.
     * @param maxPrecision max precision of currency.
     */
    public Currency(String id, String name, String minSize, String status, String message, CurrencyDetails details, String maxPrecision) {
        this.id = id;
        this.name = name;
        this.minSize = minSize;
        this.status = status;
        this.message = message;
        this.details = details;
        this.maxPrecision = maxPrecision;
    }

    /**
     * This method checks if currency is crypto-currency.
     *
     * @return {@code boolean} value based on the condition.
     */
    public boolean isCrypto() {
        return details.getType().equals("crypto");
    }

    /**
     * Overriding toString method.
     *
     * @return {@code String} representation of this object.
     */
    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", name:'" + name + '\'' +
                ", minSize:" + minSize +
                ", details:" + details +
                ", maxPrecision:" + maxPrecision +
                '}';
    }
}

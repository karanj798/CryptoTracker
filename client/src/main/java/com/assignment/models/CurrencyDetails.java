package com.assignment.models;

import com.google.gson.annotations.SerializedName;

/**
 * This class is used for serialization.
 *
 * @author Karan
 * @version v1.0
 */
public class CurrencyDetails {
    public String type;
    public String symbol;

    @SerializedName("sort_order")
    public int sortOder;

    @SerializedName("crypto_address_link")
    public String cryptoAddressLink;

    @SerializedName("crypto_transaction_link")
    public String cryptoTransactionLink;

    /**
     * Initializes all the instance variables.
     *
     * @param type type of the currency.
     * @param symbol symbol of the currency.
     * @param sortOder sort order of the currency.
     * @param cryptoAddressLink path to currency.
     * @param cryptoTransactionLink path to transaction link.
     */
    public CurrencyDetails(String type, String symbol, int sortOder, String cryptoAddressLink, String cryptoTransactionLink) {
        this.type = type;
        this.symbol = symbol;
        this.sortOder = sortOder;
        this.cryptoAddressLink = cryptoAddressLink;
        this.cryptoTransactionLink = cryptoTransactionLink;
    }

    /**
     * Overriding toString method.
     *
     * @return {@code String} representation of this object.
     */
    @Override
    public String toString() {
        return "{" +
                "type:'" + type + '\'' +
                ", symbol:'" + symbol + '\'' +
                ", sortOder:" + sortOder +
                ", cryptoAddressLink:'" + cryptoAddressLink + '\'' +
                ", cryptoTransactionLink:'" + cryptoTransactionLink + '\'' +
                '}';
    }
}

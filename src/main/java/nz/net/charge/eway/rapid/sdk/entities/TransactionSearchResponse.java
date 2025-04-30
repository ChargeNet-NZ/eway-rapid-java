package nz.net.charge.eway.rapid.sdk.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import nz.net.charge.eway.rapid.sdk.beans.internal.Transaction;

/**
 * The response from a transaction search
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionSearchResponse extends Response {

    @JsonProperty("Transactions")
    private Transaction[] transactions;

    @JsonProperty("Errors")
    private String error;

    public Transaction[] getTransactions() {
        return transactions;
    }

    @JsonProperty("Transactions")
    public void setTransactions(Transaction[] transactions) {
        this.transactions = transactions;
    }

    /**
     * Any errors that occurred processing the transaction
     *
     * @return Errors
     */
    public String getError() {
        return error;
    }

    @JsonProperty("Errors")
    public void setError(String error) {
        this.error = error;
    }

}

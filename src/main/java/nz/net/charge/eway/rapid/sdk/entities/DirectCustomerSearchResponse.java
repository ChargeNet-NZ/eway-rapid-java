package nz.net.charge.eway.rapid.sdk.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import nz.net.charge.eway.rapid.sdk.beans.internal.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * The response from searching for a customer
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectCustomerSearchResponse extends Response {

    @JsonProperty("Customers")
    private List<Customer> customers;

    @JsonProperty("Errors")
    private String errors;

    public DirectCustomerSearchResponse() {
        customers = new ArrayList<>();
    }

    /**
     * A list of customers returned by the customer search query
     * 
     * @return a List of Customers
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    /**
     * Any errors that occurred processing the transaction
     *
     * @return Errors
     */
    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

}

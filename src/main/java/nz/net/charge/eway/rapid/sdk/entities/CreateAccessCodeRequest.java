package nz.net.charge.eway.rapid.sdk.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import nz.net.charge.eway.rapid.sdk.beans.external.LineItem;
import nz.net.charge.eway.rapid.sdk.beans.internal.Customer;
import nz.net.charge.eway.rapid.sdk.beans.internal.Option;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.beans.internal.ShippingAddress;

public class CreateAccessCodeRequest extends Request {

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("Customer")
    private Customer customer;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("ShippingAddress")
    private ShippingAddress shippingAddress;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("ShippingMethod")
    private String shippingMethod;

    @JsonProperty("Items")
    private LineItem[] items = new LineItem[0];

    @JsonProperty("Options")
    private Option[] options = new Option[0];

    @JsonProperty("Payment")
    private Payment payment;

    @JsonProperty("RedirectUrl")
    private String redirectUrl;

    @JsonProperty("Method")
    private String method;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("CustomerIP")
    private String customerIP;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("DeviceID")
    private String deviceID;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("CheckoutPayment")
    private Boolean checkoutPayment;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("CheckoutUrl")
    private String checkoutUrl;

    @JsonProperty("TransactionType")
    private String transactionType;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("PartnerID")
    private String partnerID;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public LineItem[] getItems() {
        return items;
    }

    public void setItems(LineItem[] items) {
        this.items = items;
    }

    public Option[] getOptions() {
        return options;
    }

    public void setOptions(Option[] options) {
        this.options = options;
    }

    public Payment getPayment() {
        if (payment == null) {
            payment = new Payment();
        }
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCustomerIP() {
        return customerIP;
    }

    public void setCustomerIP(String customerIP) {
        this.customerIP = customerIP;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Boolean getCheckoutPayment() {
        return checkoutPayment;
    }

    public void setCheckoutPayment(Boolean checkoutPayment) {
        this.checkoutPayment = checkoutPayment;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

}

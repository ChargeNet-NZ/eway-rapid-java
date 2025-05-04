package nz.net.charge.eway.rapid.sdk.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;

public class CapturePaymentRequest extends Request {

    @JsonProperty("TransactionId")
    private String transactionId;

    @JsonProperty("Payment")
    public Payment payment;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}

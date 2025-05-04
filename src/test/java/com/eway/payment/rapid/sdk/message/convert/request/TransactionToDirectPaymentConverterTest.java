package com.eway.payment.rapid.sdk.message.convert.request;

import com.eway.payment.rapid.sdk.beans.external.Address;
import com.eway.payment.rapid.sdk.beans.external.CardDetails;
import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.beans.external.PaymentDetails;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.beans.external.TransactionType;
import com.eway.payment.rapid.sdk.entities.DirectPaymentRequest;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.message.convert.BeanConverter;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionToDirectPaymentConverterTest {

    private BeanConverter<Transaction, DirectPaymentRequest> convert;
    Transaction input;

    @BeforeEach
    public void setup() {
        convert = new TransactionToDirectPaymentConverter();
        Customer customer = ObjectCreator.createExternalCustomer();
        Address address = ObjectCreator.createAddress();
        CardDetails cardDetails = ObjectCreator.createCardDetails();
        PaymentDetails paymentDetails = ObjectCreator.createPaymentDetails();
        input = new Transaction();
        customer.setAddress(address);
        customer.setCardDetails(cardDetails);
        input.setCustomer(customer);
        input.setPaymentDetails(paymentDetails);
        input.setTransactionType(TransactionType.Purchase);
        input.setCapture(true);
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        DirectPaymentRequest request = convert.doConvert(input);
        assertThat(request.getPayment().getTotalAmount()).isEqualTo(1000);
        assertThat(request.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(request.getCustomer().getStreet1()).isEqualTo("Level 5");
        assertThat(request.getCustomer().getCardDetails().getExpiryMonth()).isEqualTo("12");
        assertThat(request.getTransactionType()).isEqualTo(TransactionType.Purchase.name());

    }
}

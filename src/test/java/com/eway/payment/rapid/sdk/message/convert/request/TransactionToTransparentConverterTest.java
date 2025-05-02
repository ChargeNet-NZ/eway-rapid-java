package com.eway.payment.rapid.sdk.message.convert.request;

import com.eway.payment.rapid.sdk.beans.external.Address;
import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.beans.external.PaymentDetails;
import com.eway.payment.rapid.sdk.beans.external.RequestMethod;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.beans.external.TransactionType;
import com.eway.payment.rapid.sdk.entities.CreateAccessCodeRequest;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.message.convert.BeanConverter;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionToTransparentConverterTest {

    private BeanConverter<Transaction, CreateAccessCodeRequest> convert;
    Transaction input;

    @BeforeEach
    public void setup() {
        convert = new TransactionToCreateAccessCodeRequestConverter();
        Customer customer = ObjectCreator.createExternalCustomer();
        Address address = ObjectCreator.createAddress();
        PaymentDetails paymentDetails = ObjectCreator.createPaymentDetails();
        input = new Transaction();
        customer.setAddress(address);
        input.setCustomer(customer);
        input.setPaymentDetails(paymentDetails);
        input.setTransactionType(TransactionType.Purchase);
        input.setCapture(true);
        input.setRedirectURL("http://www.eway.com.au");
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        CreateAccessCodeRequest request = convert.doConvert(input);
        assertThat(request.getPayment().getTotalAmount()).isEqualTo(1000);
        assertThat(request.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(request.getCustomer().getStreet1()).isEqualTo("Level 5");
        assertThat(request.getTransactionType()).isEqualTo(TransactionType.Purchase.name());

    }
    
    @Test
    public void testDoConvertAuthorise() throws RapidSdkException {
        input.setCapture(false);
        CreateAccessCodeRequest request = convert.doConvert(input);
        assertThat(request.getPayment().getTotalAmount()).isEqualTo(1000);
        assertThat(request.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(request.getCustomer().getStreet1()).isEqualTo("Level 5");
        assertThat(request.getTransactionType()).isEqualTo(TransactionType.Purchase.name());
        assertThat(request.getMethod()).isEqualTo(RequestMethod.Authorise.name());

    }
    
    @Test
    public void testDoConvertSaveCustomer() throws RapidSdkException {
        input.setSaveCustomer(true);
        CreateAccessCodeRequest request = convert.doConvert(input);
        assertThat(request.getPayment().getTotalAmount()).isEqualTo(1000);
        assertThat(request.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(request.getCustomer().getStreet1()).isEqualTo("Level 5");
        assertThat(request.getTransactionType()).isEqualTo(TransactionType.Purchase.name());
        assertThat(request.getMethod()).isEqualTo(RequestMethod.TokenPayment.name());

    }
}

package com.eway.payment.rapid.sdk.integration.transaction;

import com.eway.payment.rapid.sdk.InputModelFactory;
import com.eway.payment.rapid.sdk.RapidClient;
import com.eway.payment.rapid.sdk.beans.external.Address;
import com.eway.payment.rapid.sdk.beans.external.CardDetails;
import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.beans.external.PaymentDetails;
import com.eway.payment.rapid.sdk.beans.external.PaymentMethod;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.integration.IntegrationTest;
import com.eway.payment.rapid.sdk.output.CreateTransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationTransactionTest extends IntegrationTest {

    RapidClient client;
    Transaction t;

    @BeforeEach
    public void setup() {
        client = getSandboxClient();
        t = InputModelFactory.createTransaction();
        Customer c = InputModelFactory.initCustomer();
        Address a = InputModelFactory.initAddress();
        PaymentDetails p = InputModelFactory.initPaymentDetails();
        final String futureYear = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        CardDetails cd = InputModelFactory.initCardDetails("12", futureYear);
        c.setCardDetails(cd);
        c.setAddress(a);
        t.setCustomer(c);
        t.setPaymentDetails(p);
    }

    @Test
    public void testValidInput() {
        t.setCapture(false);
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t);
        t.setAuthTransactionID(res.getTransactionStatus().getTransactionID());
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t);
        assertThat(res.getTransactionStatus().isStatus()).isTrue();
        assertThat(authRes.getTransactionStatus().getTransactionID()).isNotEqualTo(0);
    }

    @Test
    public void testInvalidInput1() {
        t.setAuthTransactionID(1234);
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t);
        assertThat(authRes.getErrors().get(0)).contains("V6134");
    }

    @Test
    public void testInvalidInput2() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t);
        t.setAuthTransactionID(res.getTransactionStatus().getTransactionID());
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t);
        CreateTransactionResponse authRes2 = client.create(PaymentMethod.Authorisation, t);
        assertThat(authRes2.getTransactionStatus().isStatus()).isFalse();
    }

}

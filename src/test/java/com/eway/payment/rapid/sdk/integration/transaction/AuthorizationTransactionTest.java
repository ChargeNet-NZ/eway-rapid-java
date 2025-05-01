package com.eway.payment.rapid.sdk.integration.transaction;

import com.eway.payment.rapid.sdk.InputModelFactory;
import com.eway.payment.rapid.sdk.RapidClient;
import com.eway.payment.rapid.sdk.beans.external.*;
import com.eway.payment.rapid.sdk.integration.IntegrationTest;
import com.eway.payment.rapid.sdk.output.CreateTransactionResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AuthorizationTransactionTest extends IntegrationTest {

    RapidClient client;
    Transaction t;

    @Before
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
        Assert.assertTrue(res.getTransactionStatus().isStatus());
        Assert.assertNotEquals(0, authRes.getTransactionStatus().getTransactionID());
    }

    @Test
    public void testInvalidInput1() {
        t.setAuthTransactionID(1234);
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t);
        Assert.assertTrue(authRes.getErrors().get(0).contains("V6134"));
    }

    @Test
    public void testInvalidInput2() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t);
        t.setAuthTransactionID(res.getTransactionStatus().getTransactionID());
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t);
        CreateTransactionResponse authRes2 = client.create(PaymentMethod.Authorisation, t);
        Assert.assertTrue(!authRes2.getTransactionStatus().isStatus());
    }

    @After
    public void tearDown() {

    }

}

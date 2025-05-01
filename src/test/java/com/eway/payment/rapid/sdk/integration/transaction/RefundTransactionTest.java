package com.eway.payment.rapid.sdk.integration.transaction;

import com.eway.payment.rapid.sdk.InputModelFactory;
import com.eway.payment.rapid.sdk.RapidClient;
import com.eway.payment.rapid.sdk.beans.external.*;
import com.eway.payment.rapid.sdk.beans.internal.RefundDetails;
import com.eway.payment.rapid.sdk.integration.IntegrationTest;
import com.eway.payment.rapid.sdk.output.CreateTransactionResponse;
import com.eway.payment.rapid.sdk.output.RefundResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RefundTransactionTest extends IntegrationTest {

    RapidClient client;
    Transaction t;
    Refund refund;

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
        refund = new Refund();
        refund.setCustomer(c);

    }

    @Test
    public void testValidInput() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t);
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID(String.valueOf(res.getTransactionStatus().getTransactionID()));
        rd.setTotalAmount(res.getTransactionStatus().getTotal());
        refund.setRefundDetails(rd);
        refund.getCustomer().getCardDetails().setCVN(null);
        refund.getCustomer().getCardDetails().setIssueNumber(null);
        refund.getCustomer().getCardDetails().setName(null);
        refund.getCustomer().getCardDetails().setNumber(null);
        refund.getCustomer().getCardDetails().setStartMonth(null);
        refund.getCustomer().getCardDetails().setStartYear(null);
        RefundResponse refundRes = client.refund(refund);
        Assert.assertTrue(refundRes.getTransactionStatus().isStatus());
    }

    @Test
    public void testInvalidInput1() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t);
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID(String.valueOf(res.getTransactionStatus().getTransactionID()));
        rd.setTotalAmount(1001);
        refund.setRefundDetails(rd);
        refund.getCustomer().getCardDetails().setCVN(null);
        refund.getCustomer().getCardDetails().setIssueNumber(null);
        refund.getCustomer().getCardDetails().setName(null);
        refund.getCustomer().getCardDetails().setNumber(null);
        refund.getCustomer().getCardDetails().setStartMonth(null);
        refund.getCustomer().getCardDetails().setStartYear(null);
        RefundResponse refundRes = client.refund(refund);
        Assert.assertTrue(refundRes.getErrors().contains("V6151"));
    }

    @Test
    public void testInvalidInput2() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t);
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID(String.valueOf(res.getTransactionStatus().getTransactionID()));
        refund.setRefundDetails(rd);
        refund.getCustomer().getCardDetails().setCVN(null);
        refund.getCustomer().getCardDetails().setIssueNumber(null);
        refund.getCustomer().getCardDetails().setName(null);
        refund.getCustomer().getCardDetails().setNumber(null);
        refund.getCustomer().getCardDetails().setStartMonth(null);
        refund.getCustomer().getCardDetails().setStartYear(null);
        RefundResponse refundRes = client.refund(refund);
        refund.getRefundDetails().setOriginalTransactionID(String.valueOf(refundRes.getTransactionStatus().getTransactionID()));
        RefundResponse refundRes2 = client.refund(refund);
        Assert.assertTrue(refundRes2.getErrors().contains("V6113"));
    }

    @Test
    public void testInvalidInput3() {
        client.create(PaymentMethod.Direct, t);
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID("abcd");
        refund.setRefundDetails(rd);
        refund.getCustomer().getCardDetails().setCVN(null);
        refund.getCustomer().getCardDetails().setIssueNumber(null);
        refund.getCustomer().getCardDetails().setName(null);
        refund.getCustomer().getCardDetails().setNumber(null);
        refund.getCustomer().getCardDetails().setStartMonth(null);
        refund.getCustomer().getCardDetails().setStartYear(null);
        RefundResponse refundRes = client.refund(refund);
        Assert.assertTrue(refundRes.getErrors().contains("V6115"));
    }

    @Test
    public void testInvalidInput4() {
        client.create(PaymentMethod.Direct, t);
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID(null);
        refund.setRefundDetails(rd);
        refund.getCustomer().getCardDetails().setCVN(null);
        refund.getCustomer().getCardDetails().setIssueNumber(null);
        refund.getCustomer().getCardDetails().setName(null);
        refund.getCustomer().getCardDetails().setNumber(null);
        refund.getCustomer().getCardDetails().setStartMonth(null);
        refund.getCustomer().getCardDetails().setStartYear(null);
        RefundResponse refundRes = client.refund(refund);
        Assert.assertTrue(refundRes.getErrors().contains("V6115"));
    }

    @After
    public void tearDown() {

    }

}

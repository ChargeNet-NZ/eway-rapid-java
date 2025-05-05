package nz.net.charge.eway.rapid.sdk.integration.transaction;

import nz.net.charge.eway.rapid.sdk.InputModelFactory;
import nz.net.charge.eway.rapid.sdk.RapidClient;
import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentMethod;
import nz.net.charge.eway.rapid.sdk.beans.external.Refund;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.RefundDetails;
import nz.net.charge.eway.rapid.sdk.integration.IntegrationTest;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import nz.net.charge.eway.rapid.sdk.output.RefundResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class RefundTransactionTest extends IntegrationTest {

    RapidClient client;
    Transaction t;
    Refund refund;

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
        refund = new Refund();
        refund.setCustomer(c);

    }

    @Test
    public void testValidInput() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t).block();
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
        RefundResponse refundRes = client.refund(refund).block();
        assertThat(refundRes.getTransactionStatus().isStatus()).isTrue();
    }

    @Test
    public void testInvalidInput1() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t).block();
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
        RefundResponse refundRes = client.refund(refund).block();
        assertThat(refundRes.getErrors()).contains("V6151");
    }

    @Test
    public void testInvalidInput2() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t).block();
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID(String.valueOf(res.getTransactionStatus().getTransactionID()));
        refund.setRefundDetails(rd);
        refund.getCustomer().getCardDetails().setCVN(null);
        refund.getCustomer().getCardDetails().setIssueNumber(null);
        refund.getCustomer().getCardDetails().setName(null);
        refund.getCustomer().getCardDetails().setNumber(null);
        refund.getCustomer().getCardDetails().setStartMonth(null);
        refund.getCustomer().getCardDetails().setStartYear(null);
        RefundResponse refundRes = client.refund(refund).block();
        refund.getRefundDetails().setOriginalTransactionID(String.valueOf(refundRes.getTransactionStatus().getTransactionID()));
        RefundResponse refundRes2 = client.refund(refund).block();
        assertThat(refundRes2.getErrors()).contains("V6113");
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
        RefundResponse refundRes = client.refund(refund).block();
        assertThat(refundRes.getErrors()).contains("V6115");
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
        RefundResponse refundRes = client.refund(refund).block();
        assertThat(refundRes.getErrors()).contains("V6115");
    }

}

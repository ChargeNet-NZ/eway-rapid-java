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

public class CancelTransactionTest extends IntegrationTest {

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
        t.setCapture(false);
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t).block();
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID(String.valueOf(res.getTransactionStatus().getTransactionID()));
        refund.setRefundDetails(rd);
        RefundResponse cancelRes = client.cancel(refund).block();
        assertThat(cancelRes.getTransactionStatus().isStatus()).isTrue();
    }

    @Test
    public void testInvalidInput1() {
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID("20400723");
        refund.setRefundDetails(rd);
        RefundResponse cancelRes = client.cancel(refund).block();
        assertThat(cancelRes.getErrors()).contains("V6134");
    }

    @Test
    public void testInvalidInput2() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t).block();
        t.setAuthTransactionID(res.getTransactionStatus().getTransactionID());
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t).block();
        RefundDetails rd = new RefundDetails();
        rd.setOriginalTransactionID(String.valueOf(res.getTransactionStatus().getTransactionID()));
        refund.setRefundDetails(rd);
        RefundResponse cancelRes = client.cancel(refund).block();
        assertThat(cancelRes.getTransactionStatus().isStatus()).isFalse();
    }

}

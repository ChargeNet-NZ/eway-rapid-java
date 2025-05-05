package nz.net.charge.eway.rapid.sdk.integration.transaction;

import nz.net.charge.eway.rapid.sdk.InputModelFactory;
import nz.net.charge.eway.rapid.sdk.RapidClient;
import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentMethod;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.integration.IntegrationTest;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
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
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t).block();
        t.setAuthTransactionID(res.getTransactionStatus().getTransactionID());
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t).block();
        assertThat(res.getTransactionStatus().isStatus()).isTrue();
        assertThat(authRes.getTransactionStatus().getTransactionID()).isNotEqualTo(0);
    }

    @Test
    public void testInvalidInput1() {
        t.setAuthTransactionID(1234);
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t).block();
        assertThat(authRes.getErrors().get(0)).contains("V6134");
    }

    @Test
    public void testInvalidInput2() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t).block();
        t.setAuthTransactionID(res.getTransactionStatus().getTransactionID());
        CreateTransactionResponse authRes = client.create(PaymentMethod.Authorisation, t).block();
        CreateTransactionResponse authRes2 = client.create(PaymentMethod.Authorisation, t).block();
        assertThat(authRes2.getTransactionStatus().isStatus()).isFalse();
    }

}

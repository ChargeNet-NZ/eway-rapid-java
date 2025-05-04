package nz.net.charge.eway.rapid.sdk.integration.transaction;

import nz.net.charge.eway.rapid.sdk.InputModelFactory;
import nz.net.charge.eway.rapid.sdk.RapidClient;
import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentMethod;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionType;
import nz.net.charge.eway.rapid.sdk.integration.IntegrationTest;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponsiveTransactionTest extends IntegrationTest {

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
        t.setCustomView("bootstrap");
        t.setHeaderText("My Site Header Text");
        t.setCustomerReadOnly(false);
        t.setLogoUrl("https://mysite.com/images/logo4eway.jpg");
        t.setVerifyCustomerEmail(false);
        t.setVerifyCustomerPhone(false);
        t.setLanguage("EN");
        t.setCheckoutPayment(false);
    }

    @Test
    public void testValidInput() {
        CreateTransactionResponse res = client.create(PaymentMethod.ResponsiveShared, t);
        assertThat(res.getSharedPaymentUrl()).isNotNull();
    }

    @Test
    public void testMinimalValidInput() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setTotalAmount(1000);

        Transaction transaction = new Transaction();
        transaction.setPaymentDetails(paymentDetails);
        transaction.setTransactionType(TransactionType.Purchase);
        transaction.setRedirectURL("http://www.eway.com.au");
        transaction.setCancelURL("http://www.eway.com.au");

        CreateTransactionResponse res = client.create(PaymentMethod.ResponsiveShared, transaction);
        assertThat(res.getSharedPaymentUrl()).isNotNull();
    }

    @Test
    public void testBlankInput() {
        Transaction tran = new Transaction();
        Customer c = new Customer();
        CardDetails cd = new CardDetails();
        c.setCardDetails(cd);
        tran.setCustomer(c);
        tran.setTransactionType(TransactionType.Purchase);
        CreateTransactionResponse res = client.create(PaymentMethod.ResponsiveShared, tran);
        assertThat(res.getErrors() != null && !res.getErrors().isEmpty()).isTrue();
    }

}

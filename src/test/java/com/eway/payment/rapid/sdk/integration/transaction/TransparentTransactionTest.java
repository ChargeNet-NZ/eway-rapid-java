package com.eway.payment.rapid.sdk.integration.transaction;

import com.eway.payment.rapid.sdk.InputModelFactory;
import com.eway.payment.rapid.sdk.RapidClient;
import com.eway.payment.rapid.sdk.beans.external.Address;
import com.eway.payment.rapid.sdk.beans.external.CardDetails;
import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.beans.external.PaymentDetails;
import com.eway.payment.rapid.sdk.beans.external.PaymentMethod;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.beans.external.TransactionType;
import com.eway.payment.rapid.sdk.integration.IntegrationTest;
import com.eway.payment.rapid.sdk.output.CreateTransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class TransparentTransactionTest extends IntegrationTest {

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
        CreateTransactionResponse res = client.create(PaymentMethod.TransparentRedirect, t);
        assertThat(res.getFormActionUrl()).isNotNull();
    }

    @Test
    public void testMinimalValidInput() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setTotalAmount(1000);

        Transaction transaction = new Transaction();
        transaction.setPaymentDetails(paymentDetails);
        transaction.setTransactionType(TransactionType.Purchase);
        transaction.setRedirectURL("http://www.eway.com.au");

        CreateTransactionResponse res = client.create(PaymentMethod.TransparentRedirect, transaction);
        assertThat(res.getFormActionUrl()).isNotNull();
    }

    @Test
    public void testBlankInput() {
        Transaction tran = new Transaction();
        Customer c = new Customer();
        CardDetails cd = new CardDetails();
        c.setCardDetails(cd);
        tran.setCustomer(c);
        tran.setTransactionType(TransactionType.Purchase);
        CreateTransactionResponse res = client.create(PaymentMethod.TransparentRedirect, tran);
        assertThat(res.getErrors()).contains("V6047");
    }

    /**
     *
     */
    @Test
    public void testInvalidInput() {
//		t.setCustomer(null);
        t.getPaymentDetails().setTotalAmount(-1000);
        t.getCustomer().getCardDetails().setExpiryMonth("13");
        CreateTransactionResponse res = client.create(PaymentMethod.TransparentRedirect, t);
        assertThat(res.getErrors()).contains("V6011");
    }

}

package com.eway.payment.rapid.sdk.integration.transaction;

import com.eway.payment.rapid.sdk.InputModelFactory;
import com.eway.payment.rapid.sdk.RapidClient;
import com.eway.payment.rapid.sdk.beans.external.Address;
import com.eway.payment.rapid.sdk.beans.external.CardDetails;
import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.beans.external.FraudAction;
import com.eway.payment.rapid.sdk.beans.external.PaymentDetails;
import com.eway.payment.rapid.sdk.beans.external.PaymentMethod;
import com.eway.payment.rapid.sdk.beans.external.ShippingDetails;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.beans.external.TransactionType;
import com.eway.payment.rapid.sdk.integration.IntegrationTest;
import com.eway.payment.rapid.sdk.output.CreateTransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectTransactionTest extends IntegrationTest {

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
        ShippingDetails shipping = InputModelFactory.createShippingDetail();
        t.setShippingDetails(shipping);
    }

    @Test
    public void testValidInput() {
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t);
        assertThat(res.getTransactionStatus().isStatus()).isTrue();
        assertThat(res.getTransactionStatus().getTransactionID()).isNotEqualTo(0);
    }

    @Test
    public void testMinimalValidInput() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setTotalAmount(1000);

        final String futureYear = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        CardDetails cd = InputModelFactory.initCardDetails("12", futureYear);
        Customer customer = new Customer();
        customer.setCardDetails(cd);

        Transaction transaction = new Transaction();
        transaction.setPaymentDetails(paymentDetails);
        transaction.setCustomer(customer);
        transaction.setTransactionType(TransactionType.Purchase);

        CreateTransactionResponse res = client.create(PaymentMethod.Direct, transaction);

        assertThat(res.getTransactionStatus().isStatus()).isTrue();
        assertThat(res.getTransactionStatus().getTransactionID()).isNotEqualTo(0);
        assertThat(FraudAction.NotChallenged.name()).isEqualTo(res.getTransactionStatus().getFraudAction().name());
    }

    @Test
    public void testBlankInput() {  
        Transaction tran = new Transaction();
        Customer c = new Customer();
        CardDetails cd = new CardDetails();
        c.setCardDetails(cd);
        tran.setCustomer(c);
        tran.setTransactionType(TransactionType.Purchase);
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, tran);

        assertThat(res.getTransactionStatus().isStatus()).isFalse();
        assertThat(res.getTransactionStatus().getTransactionID()).isEqualTo(0);
        assertThat(res.getErrors())
                .contains("V6021")
//        Assert.assertTrue(res.getErrors().contains("V6022")); //problem with Rapid not returning the correct errors
                .contains("V6101")
                .contains("V6102")
                .contains("V6023");
        assertThat(FraudAction.NotChallenged.name()).isEqualTo(res.getTransactionStatus().getFraudAction().name());
    }

    @Test
    public void testInvalidInput() {
        t.getCustomer().getCardDetails().setExpiryMonth("13");
        CreateTransactionResponse res = client.create(PaymentMethod.Direct, t);
        assertThat(res.getTransactionStatus().isStatus()).isFalse();
        assertThat(res.getTransactionStatus().getTransactionID()).isEqualTo(0);
        assertThat(res.getErrors()).contains("V6101");
    }

}

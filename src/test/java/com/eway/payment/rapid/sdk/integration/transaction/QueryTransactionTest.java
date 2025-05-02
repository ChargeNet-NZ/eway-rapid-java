package com.eway.payment.rapid.sdk.integration.transaction;

import com.eway.payment.rapid.sdk.InputModelFactory;
import com.eway.payment.rapid.sdk.RapidClient;
import com.eway.payment.rapid.sdk.beans.external.Address;
import com.eway.payment.rapid.sdk.beans.external.CardDetails;
import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.beans.external.FraudAction;
import com.eway.payment.rapid.sdk.beans.external.PaymentDetails;
import com.eway.payment.rapid.sdk.beans.external.PaymentMethod;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.integration.IntegrationTest;
import com.eway.payment.rapid.sdk.output.CreateTransactionResponse;
import com.eway.payment.rapid.sdk.output.QueryTransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTransactionTest extends IntegrationTest {

    RapidClient client;
    Transaction trans;

    @BeforeEach
    public void setup() {
        client = getSandboxClient();

    }

    @Test
    public void testValidInput() {
        trans = InputModelFactory.createTransaction();
        Customer c = InputModelFactory.initCustomer();
        Address a = InputModelFactory.initAddress();
        PaymentDetails p = InputModelFactory.initPaymentDetails();
        final String futureYear = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        CardDetails cd = InputModelFactory.initCardDetails("12", futureYear);
        c.setCardDetails(cd);
        c.setAddress(a);
        trans.setCustomer(c);
        trans.setPaymentDetails(p);

        CreateTransactionResponse transResponse = client.create(
                PaymentMethod.Direct, trans);
        assertThat(transResponse.getTransactionStatus().isStatus()).isTrue();
        assertThat(transResponse.getTransactionStatus().getTransactionID()).isNotEqualTo(0);

        int transactionId = transResponse.getTransactionStatus()
                .getTransactionID();
        QueryTransactionResponse query = client.queryTransaction(transactionId);
        assertThat(query.getTransactionStatus().getTransactionID()).isEqualTo(transactionId);
        assertThat(query.getTransaction().getOptions()).isNotEmpty();
        assertThat(query.getErrors() == null || query.getErrors().isEmpty()).isTrue();
        assertThat(query.getTransactionStatus().getFraudAction().name()).isEqualTo(FraudAction.NotChallenged.name());

    }

    @Test
    public void testBlankInput() {
        QueryTransactionResponse res = client.queryTransaction("");
        assertThat(res.getTransactionStatus().getTransactionID() == 0 || res.getTransaction() == null).isTrue();
        assertThat(res.getTransactionStatus().getFraudAction().name()).isEqualTo(FraudAction.NotChallenged.name());
    }

    @Test
    public void testInvalidInput() {
        QueryTransactionResponse res = client
                .queryTransaction(InputModelFactory.randomString(50));
        assertThat(res.getTransaction()).isNull();
    }

}

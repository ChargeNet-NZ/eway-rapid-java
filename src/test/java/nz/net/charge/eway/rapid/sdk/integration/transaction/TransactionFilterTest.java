package nz.net.charge.eway.rapid.sdk.integration.transaction;

import nz.net.charge.eway.rapid.sdk.InputModelFactory;
import nz.net.charge.eway.rapid.sdk.RapidClient;
import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentMethod;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionFilter;
import nz.net.charge.eway.rapid.sdk.integration.IntegrationTest;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import nz.net.charge.eway.rapid.sdk.output.QueryTransactionResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionFilterTest extends IntegrationTest {

    private RapidClient client;
    private Transaction trans;

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
        CreateTransactionResponse transResponse = client.create(PaymentMethod.Direct, trans);
        assertThat(transResponse.getTransactionStatus().isStatus()).isTrue();
        assertThat(transResponse.getTransactionStatus().getTransactionID()).isNotEqualTo(0);

        TransactionFilter filter = new TransactionFilter();
        filter.setTransactionId(transResponse.getTransactionStatus().getTransactionID());

        QueryTransactionResponse res = client.queryTransaction(filter);
        assertThat(res.getTransactionStatus().isStatus()).isTrue();
        assertThat(res.getTransactionStatus().getTransactionID())
                .isEqualTo(transResponse.getTransactionStatus().getTransactionID());
    }

    @Test
    public void testBlankInput() {
        TransactionFilter filter = new TransactionFilter();
        QueryTransactionResponse res = client.queryTransaction(filter);
        assertThat(res.getTransaction()).isNull();
    }

    @Test
    public void testInvalidInput() {
        TransactionFilter filter = new TransactionFilter();
        filter.setTransactionId(11742962);
        filter.setInvoiceNumber("Inv 21540");
        QueryTransactionResponse res = client.queryTransaction(filter);
        assertThat(res.getErrors()).contains("S9991");
    }

    @Test
    public void testSearchInvoiceNumberOrReference() {
        trans = InputModelFactory.createTransaction();
        Customer c = InputModelFactory.initCustomer();
        Address a = InputModelFactory.initAddress();
        String invoiceNum = InputModelFactory.randomString(10);
        String invoiceRef = InputModelFactory.randomString(10);
        PaymentDetails p = InputModelFactory.initPaymentDetails(invoiceNum, invoiceRef);
        final String futureYear = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        CardDetails cd = InputModelFactory.initCardDetails("12", futureYear);
        c.setCardDetails(cd);
        c.setAddress(a);
        trans.setCustomer(c);
        trans.setPaymentDetails(p);
        CreateTransactionResponse transResponse = client.create(PaymentMethod.Direct, trans);
        assertThat(transResponse.getTransactionStatus().isStatus()).isTrue();
        int transacionId = transResponse.getTransactionStatus().getTransactionID();

        // search invoice number
        TransactionFilter filter = new TransactionFilter();
        filter.setInvoiceNumber(invoiceNum);
        QueryTransactionResponse res = client.queryTransaction(filter);
        assertThat(res.getTransactionStatus().isStatus()).isTrue();
        assertThat(res.getTransactionStatus().getTransactionID()).isEqualTo(transacionId);
        assertThat(res.getTransaction().getPaymentDetails().getInvoiceNumber()).isEqualTo(invoiceNum);
        // Search invoice  reference
        filter = new TransactionFilter();
        filter.setInvoiceReference(invoiceRef);
        res = client.queryTransaction(filter);
        assertThat(res.getTransactionStatus().isStatus()).isTrue();
        assertThat(res.getTransactionStatus().getTransactionID()).isEqualTo(transacionId);
        assertThat(res.getTransaction().getPaymentDetails().getInvoiceReference()).isEqualTo(invoiceRef);

		// Search access code
    }

    @Test
    public void testSearchAccessCode() {
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

        CreateTransactionResponse transResponse = client.create(PaymentMethod.TransparentRedirect, trans);
        String accessCode = transResponse.getAccessCode();
        assertThat(StringUtils.isBlank(accessCode)).isFalse();

        TransactionFilter filter = new TransactionFilter();
        filter.setAccessCode(accessCode);

        QueryTransactionResponse res = client.queryTransaction(filter);
        assertThat(res.getErrors() == null || res.getErrors().isEmpty()).isTrue();

    }

    @Test
    public void testSearchMissingInvoiceNumber() {
        String invoiceNum = InputModelFactory.randomString(100);
        // search invoice number
        TransactionFilter filter = new TransactionFilter();
        filter.setInvoiceNumber(invoiceNum);
        QueryTransactionResponse res = client.queryTransaction(filter);
        assertThat(res.getErrors() != null && !res.getErrors().isEmpty()).isTrue();
        assertThat(res.getErrors()).contains("V6171");
    }

    @Test
    public void testMissingTransactionFilterObject() {
        // assign two field of transaction filter
        String invoiceNum = InputModelFactory.randomString(100);
        String invoiceRef = InputModelFactory.randomString(100);
        TransactionFilter filter = new TransactionFilter();
        filter.setInvoiceNumber(invoiceNum);
        filter.setInvoiceReference(invoiceRef);
        QueryTransactionResponse res = client.queryTransaction(filter);
        // expect error code null or empty and contain S9991: invalid parameter
        assertThat(res.getErrors() != null && !res.getErrors().isEmpty()).isTrue();
    }

}

package nz.net.charge.eway.rapid.sdk.message.convert.request;

import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionType;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentRequest;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionToDirectPaymentConverterTest {

    private BeanConverter<Transaction, DirectPaymentRequest> convert;
    Transaction input;

    @BeforeEach
    public void setup() {
        convert = new TransactionToDirectPaymentConverter();
        Customer customer = ObjectCreator.createExternalCustomer();
        Address address = ObjectCreator.createAddress();
        CardDetails cardDetails = ObjectCreator.createCardDetails();
        PaymentDetails paymentDetails = ObjectCreator.createPaymentDetails();
        input = new Transaction();
        customer.setAddress(address);
        customer.setCardDetails(cardDetails);
        input.setCustomer(customer);
        input.setPaymentDetails(paymentDetails);
        input.setTransactionType(TransactionType.Purchase);
        input.setCapture(true);
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        DirectPaymentRequest request = convert.doConvert(input);
        assertThat(request.getPayment().getTotalAmount()).isEqualTo(1000);
        assertThat(request.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(request.getCustomer().getStreet1()).isEqualTo("Level 5");
        assertThat(request.getCustomer().getCardDetails().getExpiryMonth()).isEqualTo("12");
        assertThat(request.getTransactionType()).isEqualTo(TransactionType.Purchase.name());

    }
}

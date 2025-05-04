package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionToPaymentConverterTest {

    private BeanConverter<Transaction, Payment> convert;

    @BeforeEach
    public void setup() {
        convert = new TransactionToPaymentConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        Transaction t = ObjectCreator.createTransaction();
        t.setPaymentDetails(ObjectCreator.createPaymentDetails());
        Payment payment = convert.doConvert(t);
        assertThat(payment.getTotalAmount()).isEqualTo(1000);
    }

    @Test
    public void testNullPayment() throws RapidSdkException {
        Transaction t = ObjectCreator.createTransaction();
        Payment payment = convert.doConvert(t);
        assertThat(payment.getTotalAmount()).isEqualTo(0);
    }
}

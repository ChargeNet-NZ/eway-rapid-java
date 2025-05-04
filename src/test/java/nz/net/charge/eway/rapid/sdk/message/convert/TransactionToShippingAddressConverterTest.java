package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.ShippingAddress;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionToShippingAddressConverterTest {

    private BeanConverter<Transaction, ShippingAddress> convert;

    @BeforeEach
    public void setup() {
        convert = new TransactionShippingAddressConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        Transaction t = ObjectCreator.createTransaction();
        t.setShippingDetails(ObjectCreator.createShippingDetails());
        ShippingAddress address = convert.doConvert(t);
        assertThat(address.getCity()).isEqualTo("Sydney");

    }

    @Test
    public void testNullShippingDetail() throws RapidSdkException {
        Transaction t = ObjectCreator.createTransaction();
        ShippingAddress address = convert.doConvert(t);
        assertThat(address.getCity()).isNull();

    }

    @Test
    public void testNullAddress() throws RapidSdkException {
        Transaction t = ObjectCreator.createTransaction();
        t.setShippingDetails(ObjectCreator.createShippingDetails());
        t.getShippingDetails().setShippingAddress(null);
        ShippingAddress address = convert.doConvert(t);
        assertThat(address.getCity()).isNull();

    }
}

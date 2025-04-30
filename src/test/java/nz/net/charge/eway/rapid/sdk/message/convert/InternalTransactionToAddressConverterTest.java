package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.internal.Transaction;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalTransactionToAddressConverterTest {

    private BeanConverter<Transaction, Address> convert;

    @BeforeEach
    public void setup() {
        convert = new InternalTransactionToAddressConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        Transaction t = ObjectCreator.createInternalTransaction();
        Address address = convert.doConvert(t);
        assertThat(address.getCity()).isEqualTo("Sydney");
    }
}

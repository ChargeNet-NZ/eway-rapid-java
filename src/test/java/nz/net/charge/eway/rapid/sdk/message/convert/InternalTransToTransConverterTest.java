package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.internal.Transaction;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalTransToTransConverterTest {

    private BeanConverter<Transaction, nz.net.charge.eway.rapid.sdk.beans.external.Transaction> convert;

    @BeforeEach
    public void setup() {
        convert = new InternalTransToTransConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        Transaction internalTran = ObjectCreator.createInternalTransaction();
        internalTran.setCustomer(ObjectCreator.createInternalCustomer());
        nz.net.charge.eway.rapid.sdk.beans.external.Transaction tran = convert.doConvert(internalTran);
        assertThat(tran.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(tran.getPaymentDetails().getTotalAmount()).isEqualTo(1000);
        assertThat(tran.getShippingDetails().getShippingAddress().getCity()).isEqualTo("Sydney");
    }
}

package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalTransToTransConverterTest {

    private BeanConverter<com.eway.payment.rapid.sdk.beans.internal.Transaction, Transaction> convert;

    @BeforeEach
    public void setup() {
        convert = new InternalTransToTransConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        com.eway.payment.rapid.sdk.beans.internal.Transaction internalTran = ObjectCreator.createInternalTransaction();
        internalTran.setCustomer(ObjectCreator.createInternalCustomer());
        Transaction tran = convert.doConvert(internalTran);
        assertThat(tran.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(tran.getPaymentDetails().getTotalAmount()).isEqualTo(1000);
        assertThat(tran.getShippingDetails().getShippingAddress().getCity()).isEqualTo("Sydney");
    }
}

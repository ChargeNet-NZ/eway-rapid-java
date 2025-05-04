package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.Address;
import com.eway.payment.rapid.sdk.beans.internal.Transaction;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalTransactionToAddressConverterTest {

    private BeanConverter<com.eway.payment.rapid.sdk.beans.internal.Transaction, Address> convert;

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

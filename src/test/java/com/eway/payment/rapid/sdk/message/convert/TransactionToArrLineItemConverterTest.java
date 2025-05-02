package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.LineItem;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionToArrLineItemConverterTest {

    private BeanConverter<Transaction, LineItem[]> convert;

    @BeforeEach
    public void setup() {
        convert = new TransactionToArrLineItemConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        Transaction t = ObjectCreator.createTransaction();
        t.setLineItems(ObjectCreator.createLineItems());
        LineItem[] itemArr = convert.doConvert(t);
        assertThat(itemArr).hasSize(1);
    }

    @Test
    public void testNullItem() throws RapidSdkException {
        Transaction t = ObjectCreator.createTransaction();
        LineItem[] itemArr = convert.doConvert(t);
        assertThat(itemArr).isEmpty();
    }
}

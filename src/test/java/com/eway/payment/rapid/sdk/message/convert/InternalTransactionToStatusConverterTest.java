package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.TransactionStatus;
import com.eway.payment.rapid.sdk.beans.internal.Transaction;
import com.eway.payment.rapid.sdk.exception.ParameterInvalidException;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InternalTransactionToStatusConverterTest {

    private BeanConverter<Transaction, TransactionStatus> convert;

    @BeforeEach
    public void setup() {
        convert = new InternalTransactionToStatusConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        Transaction t = ObjectCreator.createInternalTransaction();
        TransactionStatus status = convert.doConvert(t);
        assertThat(status.getTotal()).isEqualByComparingTo(1000);
    }

    @Test
    public void testInvalidTransactionId() {

        // Given
        Transaction t = ObjectCreator.createInternalTransaction();
        t.setTransactionID("abc");

        // When
        assertThatThrownBy(() -> convert.doConvert(t))

        // Then
            .isInstanceOf(ParameterInvalidException.class);
    }
}

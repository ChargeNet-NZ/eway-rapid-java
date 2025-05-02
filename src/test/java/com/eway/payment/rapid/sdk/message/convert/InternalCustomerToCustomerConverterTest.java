package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalCustomerToCustomerConverterTest {

    private BeanConverter<com.eway.payment.rapid.sdk.beans.internal.Customer, Customer> convert;

    @BeforeEach
    public void init() {
        convert = new InternalCustomerToCustomerConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        com.eway.payment.rapid.sdk.beans.internal.Customer internalCustomer = ObjectCreator.createInternalCustomer();
        Customer customer = convert.doConvert(internalCustomer);
        assertThat(customer.getFirstName()).isEqualTo("John");
        assertThat(customer.getCardDetails().getExpiryMonth()).isEqualTo("12");

    }
}

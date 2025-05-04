package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalCustomerToCustomerConverterTest {

    private BeanConverter<nz.net.charge.eway.rapid.sdk.beans.internal.Customer, Customer> convert;

    @BeforeEach
    public void init() {
        convert = new InternalCustomerToCustomerConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        nz.net.charge.eway.rapid.sdk.beans.internal.Customer internalCustomer = ObjectCreator.createInternalCustomer();
        Customer customer = convert.doConvert(internalCustomer);
        assertThat(customer.getFirstName()).isEqualTo("John");
        assertThat(customer.getCardDetails().getExpiryMonth()).isEqualTo("12");

    }
}

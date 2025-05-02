package com.eway.payment.rapid.sdk.message.convert;

import com.eway.payment.rapid.sdk.beans.external.ShippingDetails;
import com.eway.payment.rapid.sdk.beans.external.ShippingMethod;
import com.eway.payment.rapid.sdk.beans.internal.ShippingAddress;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShippingDetailsToAddressConverterTest {

    private BeanConverter<ShippingDetails, ShippingAddress> convert;

    @BeforeEach
    public void setup() {
        convert = new ShippingDetailsToAddressConverter();
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        ShippingDetails detail = ObjectCreator.createShippingDetails();
        ShippingAddress address = convert.doConvert(detail);
        assertThat(address.getFirstName()).isEqualTo("John");
        assertThat(address.getCity()).isEqualTo("Sydney");
        assertThat(address.getShippingMethod()).isEqualTo(ShippingMethod.NextDay.name());
    }

    @Test
    public void testNullShippingDetail() throws RapidSdkException {
        ShippingDetails detail = null;
        ShippingAddress address = convert.doConvert(detail);
        assertThat(address.getFirstName()).isNull();
    }

    @Test
    public void testNullAddress() throws RapidSdkException {
        ShippingDetails detail = ObjectCreator.createShippingDetails();
        detail.setShippingAddress(null);
        detail.setShippingMethod(null);
        ShippingAddress address = convert.doConvert(detail);
        assertThat(address.getCity()).isNull();
    }
}

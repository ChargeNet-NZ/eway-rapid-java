package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.ShippingDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.ShippingMethod;
import nz.net.charge.eway.rapid.sdk.beans.internal.ShippingAddress;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.object.create.ObjectCreator;
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

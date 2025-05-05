package nz.net.charge.eway.rapid.sdk.message.convert.request;

import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.LineItem;
import nz.net.charge.eway.rapid.sdk.beans.external.Refund;
import nz.net.charge.eway.rapid.sdk.beans.external.ShippingDetails;
import nz.net.charge.eway.rapid.sdk.beans.internal.Option;
import nz.net.charge.eway.rapid.sdk.beans.internal.RefundDetails;
import nz.net.charge.eway.rapid.sdk.entities.DirectRefundRequest;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.object.create.ObjectCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RefundToDirectRefundReqConverterTest {

    private BeanConverter<Refund, DirectRefundRequest> convert;
    Refund refund;

    @BeforeEach
    public void setup() {
        convert = new RefundToDirectRefundReqConverter();
        refund = new Refund();
        RefundDetails refundDetails = ObjectCreator.createRefundDetails();
        Customer customer = ObjectCreator.createExternalCustomer();
        Address address = ObjectCreator.createAddress();
        CardDetails cardDetails = ObjectCreator.createCardDetails();
        customer.setCardDetails(cardDetails);
        customer.setAddress(address);
        ShippingDetails shippingDetails = new ShippingDetails();
        shippingDetails.setShippingAddress(address);
        List<LineItem> items = ObjectCreator.createLineItems();
        refund.setLineItems(items);
        List<Option> options = ObjectCreator.createOptions();
        refund.setOptions(options);
        refund.setCustomer(customer);
        refund.setShippingDetails(shippingDetails);
        refund.setRefundDetails(refundDetails);
        refund.setDeviceID("DZPC");
        refund.setPartnerID("ID");
    }

    @Test
    public void testDoConvert() throws RapidSdkException {
        DirectRefundRequest request = convert.doConvert(refund);
        assertThat(request.getCustomer().getFirstName()).isEqualTo("John");
        assertThat(request.getShippingAddress().getCity()).isEqualTo("Sydney");
        assertThat(request.getItems()).hasSize(1);
        assertThat(request.getOptions()).hasSize(2);
    }
}

package nz.net.charge.eway.rapid.sdk.message.convert.request;

import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.LineItem;
import nz.net.charge.eway.rapid.sdk.beans.external.Refund;
import nz.net.charge.eway.rapid.sdk.beans.external.ShippingDetails;
import nz.net.charge.eway.rapid.sdk.beans.internal.Customer;
import nz.net.charge.eway.rapid.sdk.beans.internal.Option;
import nz.net.charge.eway.rapid.sdk.beans.internal.ShippingAddress;
import nz.net.charge.eway.rapid.sdk.entities.DirectRefundRequest;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.CustomerToInternalCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.ShippingDetailsToAddressConverter;

import java.util.ArrayList;
import java.util.List;

public class RefundToDirectRefundReqConverter implements BeanConverter<Refund, DirectRefundRequest> {

    public DirectRefundRequest doConvert(Refund refund) throws RapidSdkException {
        DirectRefundRequest request = new DirectRefundRequest();
        if (refund != null) {
            request.setRefund(refund.getRefundDetails());
            BeanConverter<ShippingDetails, ShippingAddress> shippingConvert = new ShippingDetailsToAddressConverter();
            request.setShippingAddress(shippingConvert.doConvert(refund.getShippingDetails()));
            CustomerToInternalCustomerConverter customerConvert = new CustomerToInternalCustomerConverter();
            Customer customer = customerConvert.doConvert(refund.getCustomer());
            if (customer.getCardDetails() == null) {
                customer.setCardDetails(new CardDetails());
            }
            request.setCustomer(customer);
            request.setPartnerID(refund.getPartnerID());
            request.setDeviceID(refund.getDeviceID());
            List<LineItem> lineItems = refund.getLineItems();
            if (lineItems != null) {
                LineItem[] items = lineItems.toArray(new LineItem[lineItems.size()]);
                request.setItems(items);
            }
            List<Option> listOptions = refund.getOptions();
            if (listOptions != null && !listOptions.isEmpty()) {
                List<Option> listConvert = new ArrayList<>();
                for (Option value : listOptions) {
                    Option op = new Option();
                    op.setValue(value.getValue());
                    listConvert.add(op);
                }
                request.setOptions(listConvert.toArray(new Option[listConvert.size()]));
            }
        }
        return request;
    }

}

package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.Refund;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionStatus;
import nz.net.charge.eway.rapid.sdk.entities.DirectRefundResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.DirectRefundToTransStatusConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalCustomerToCustomerConverter;
import nz.net.charge.eway.rapid.sdk.output.RefundResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class DirectRefundToRefundResponseConverter implements BeanConverter<DirectRefundResponse, RefundResponse> {

    public RefundResponse doConvert(DirectRefundResponse response) throws RapidSdkException {
        Refund refund = new Refund();
        refund.setRefundDetails(response.getRefund());
        nz.net.charge.eway.rapid.sdk.beans.internal.Customer innerCust = response.getCustomer();
        BeanConverter<nz.net.charge.eway.rapid.sdk.beans.internal.Customer, Customer> custConvert = new InternalCustomerToCustomerConverter();
        refund.setCustomer(custConvert.doConvert(innerCust));

        RefundResponse refundResponse = new RefundResponse();
        if (!StringUtils.isBlank(response.getErrors())) {
            refundResponse.setErrors(Arrays.asList(response.getErrors().split("\\s*,\\s*")));
        }
        refundResponse.setRefund(refund);

        BeanConverter<DirectRefundResponse, TransactionStatus> transStatusConvert = new DirectRefundToTransStatusConverter();
        refundResponse.setTransactionStatus(transStatusConvert.doConvert(response));
        return refundResponse;
    }

}

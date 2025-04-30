package nz.net.charge.eway.rapid.sdk.message.convert.request;

import nz.net.charge.eway.rapid.sdk.beans.external.LineItem;
import nz.net.charge.eway.rapid.sdk.beans.external.RequestMethod;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.Option;
import nz.net.charge.eway.rapid.sdk.beans.internal.Payment;
import nz.net.charge.eway.rapid.sdk.beans.internal.ShippingAddress;
import nz.net.charge.eway.rapid.sdk.entities.CreateAccessCodeRequest;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.CustomerToInternalCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.TransactionShippingAddressConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.TransactionToArrLineItemConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.TransactionToArrOptionConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.TransactionToPaymentConverter;
import org.apache.commons.lang3.StringUtils;

public class TransactionToCreateAccessCodeRequestConverter implements BeanConverter<Transaction, CreateAccessCodeRequest> {

    public CreateAccessCodeRequest doConvert(Transaction input) throws RapidSdkException {
        CreateAccessCodeRequest request = new CreateAccessCodeRequest();
        if (input != null) {
            request.setTransactionType(input.getTransactionType().name());
            request.setDeviceID(input.getDeviceID());
            request.setPartnerID(input.getPartnerID());
            request.setRedirectUrl(input.getRedirectURL());
            if (input.getPaymentDetails() != null) {
                request.getPayment().setTotalAmount(input.getPaymentDetails().getTotalAmount());
            }
            if (input.getCustomer() != null) {
                CustomerToInternalCustomerConverter internalCustConvert = new CustomerToInternalCustomerConverter(false);
                request.setCustomer(internalCustConvert.doConvert(input.getCustomer()));
            }

            request.setCustomerIP(input.getCustomer() != null ? input.getCustomer().getCustomerDeviceIP() : null);

            BeanConverter<Transaction, ShippingAddress> shipAddressConvert = new TransactionShippingAddressConverter();
            request.setShippingAddress(shipAddressConvert.doConvert(input));

            BeanConverter<Transaction, Payment> paymentConvert = new TransactionToPaymentConverter();
            request.setPayment(paymentConvert.doConvert(input));
            if (!StringUtils.isBlank(input.getCheckoutURL())) {
                request.setCheckoutPayment(input.isCheckoutPayment());
                request.setCheckoutUrl(input.getCheckoutURL());
            }

            BeanConverter<Transaction, LineItem[]> lineItemConvert = new TransactionToArrLineItemConverter();
            request.setItems(lineItemConvert.doConvert(input));

            BeanConverter<Transaction, Option[]> optionConverter = new TransactionToArrOptionConverter();
            request.setOptions(optionConverter.doConvert(input));

            if (input.getShippingDetails() != null && input.getShippingDetails().getShippinhgMethod() != null) {
                request.setShippingMethod(input.getShippingDetails().getShippinhgMethod().name());
            }
            request.setMethod(input.isCapture() ? (input.isSaveCustomer() ? RequestMethod.TokenPayment.name() : RequestMethod.ProcessPayment.name()) : RequestMethod.Authorise.name());
        }
        return request;
    }

}

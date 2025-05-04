package nz.net.charge.eway.rapid.sdk.message.convert.request;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.RequestMethod;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentRequest;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.CustomerToInternalCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.TransactionShippingAddressConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.TransactionToArrLineItemConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.TransactionToArrOptionConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.TransactionToPaymentConverter;

public class TransactionToDirectPaymentConverter implements BeanConverter<Transaction, DirectPaymentRequest> {

    public DirectPaymentRequest doConvert(Transaction input) throws RapidSdkException {
        DirectPaymentRequest request = new DirectPaymentRequest();
        BeanConverter<Customer, nz.net.charge.eway.rapid.sdk.beans.internal.Customer> interCustConvert = new CustomerToInternalCustomerConverter(false);
        if (input.getCustomer() != null) {
            request.setCustomerIP(input.getCustomer().getCustomerDeviceIP());
            request.setCustomer(interCustConvert.doConvert(input.getCustomer()));
        }
        TransactionToPaymentConverter paymentConvert = new TransactionToPaymentConverter();
        request.setPayment(paymentConvert.doConvert(input));

        TransactionShippingAddressConverter shipConverter = new TransactionShippingAddressConverter();
        request.setShippingAddress(shipConverter.doConvert(input));

        TransactionToArrLineItemConverter lineItemConvert = new TransactionToArrLineItemConverter();
        request.setItems(lineItemConvert.doConvert(input));

        TransactionToArrOptionConverter optionConverter = new TransactionToArrOptionConverter();
        request.setOptions(optionConverter.doConvert(input));

        request.setDeviceID(input.getDeviceID());
        request.setPartnerID(input.getPartnerID());
        request.setTransactionType(input.getTransactionType() != null ? input.getTransactionType().name() : "");
        request.setMethod(input.isCapture() ? RequestMethod.ProcessPayment.name() : RequestMethod.Authorise.name());
        request.setRedirectUrl(input.getRedirectURL());
        request.setSecuredCardData(input.getSecuredCardData());
        return request;
    }

}

package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.PaymentDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.ShippingDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.ShippingMethod;
import nz.net.charge.eway.rapid.sdk.beans.internal.Transaction;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class InternalTransToTransConverter implements BeanConverter<Transaction, nz.net.charge.eway.rapid.sdk.beans.external.Transaction> {

    public nz.net.charge.eway.rapid.sdk.beans.external.Transaction doConvert(Transaction iTransaction) throws RapidSdkException {
        nz.net.charge.eway.rapid.sdk.beans.external.Transaction transaction = new nz.net.charge.eway.rapid.sdk.beans.external.Transaction();
        transaction.setTokenCustomerID(iTransaction.getTokenCustomerID());
        transaction.setMaxRefund(iTransaction.getMaxRefund());
        transaction.setTransactionDateTime(iTransaction.getTransactionDateTime());
        transaction.setSource(iTransaction.getSource());
        transaction.setOriginalTransactionId(iTransaction.getOriginalTransactionId());
        transaction.setOptions(Arrays.asList(iTransaction.getOptions()));
        
        Customer eWayCustomer = getEwayCustomer(iTransaction);

        if (eWayCustomer.getTokenCustomerID() == null && iTransaction.getTokenCustomerID() != null) {
            eWayCustomer.setTokenCustomerID(iTransaction.getTokenCustomerID());
        }

        transaction.setCustomer(eWayCustomer);

        transaction.setPaymentDetails(getPaymentDetails(iTransaction));
        transaction.setShippingDetails(getShippingDetails(iTransaction));
        return transaction;
    }

    private Customer getEwayCustomer(Transaction iTransaction) throws RapidSdkException {
        nz.net.charge.eway.rapid.sdk.beans.internal.Customer iCustomer = iTransaction.getCustomer();
        InternalCustomerToCustomerConverter custConvert = new InternalCustomerToCustomerConverter();
        Customer customer = custConvert.doConvert(iCustomer);
        InternalTransactionToAddressConverter addressConvert = new InternalTransactionToAddressConverter();
        customer.setAddress(addressConvert.doConvert(iTransaction));
        return customer;
    }

    private PaymentDetails getPaymentDetails(Transaction iTransaction) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setTotalAmount(iTransaction.getTotalAmount());
        paymentDetails.setInvoiceReference(iTransaction.getInvoiceReference());
        paymentDetails.setInvoiceNumber(iTransaction.getInvoiceNumber());
        paymentDetails.setCurrencyCode(iTransaction.getCurrencyCode());
        return paymentDetails;
    }

    private ShippingDetails getShippingDetails(Transaction iTransaction) throws RapidSdkException {
        ShippingDetails shippingDetails = new ShippingDetails();
        if (iTransaction.getShippingAddress() != null) {
            InternalTransactionToAddressConverter addressConvert = new InternalTransactionToAddressConverter();
            shippingDetails.setShippingAddress(addressConvert.doConvert(iTransaction));
            String methodName = iTransaction.getShippingAddress().getShippingMethod();
            if (!StringUtils.isBlank(methodName)) {
                for (ShippingMethod m : ShippingMethod.values()) {
                    if (m.name().equalsIgnoreCase(methodName)) {
                        shippingDetails.setShippingMethod(m);
                        break;
                    }
                }
            }
        }
        if (iTransaction.getShippingAddress() != null) {
            shippingDetails.setEmail(iTransaction.getShippingAddress().getEmail());
            shippingDetails.setFax(iTransaction.getShippingAddress().getFax());
            shippingDetails.setLastName(iTransaction.getShippingAddress().getLastName());
            shippingDetails.setFirstName(iTransaction.getShippingAddress().getFirstName());
            shippingDetails.setPhone(iTransaction.getShippingAddress().getPhone());
        }
        return shippingDetails;
    }
}

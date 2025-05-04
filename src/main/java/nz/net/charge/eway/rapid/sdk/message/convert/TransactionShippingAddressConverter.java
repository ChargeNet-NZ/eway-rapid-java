package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.ShippingDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.internal.ShippingAddress;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;

public class TransactionShippingAddressConverter implements BeanConverter<Transaction, ShippingAddress> {

    public ShippingAddress doConvert(Transaction transaction) throws RapidSdkException {
        ShippingAddress shippingAddress = new ShippingAddress();
        ShippingDetails detail = transaction.getShippingDetails();
        if (detail != null) {
            shippingAddress.setFirstName(detail.getFirstName());
            shippingAddress.setLastName(detail.getLastName());
            shippingAddress.setShippingMethod(detail.getShippinhgMethod() != null ? detail.getShippinhgMethod().name() : null);
            shippingAddress.setEmail(detail.getEmail());
            shippingAddress.setPhone(detail.getPhone());
            shippingAddress.setFax(detail.getFax());
            Address address = detail.getShippingAddress();
            if (address != null) {
                shippingAddress.setCity(address.getCity());
                shippingAddress.setCountry(address.getCountry());
                shippingAddress.setPostalCode(address.getPostalCode());
                shippingAddress.setState(address.getState());
                shippingAddress.setStreet1(address.getStreet1());
                shippingAddress.setStreet2(address.getStreet2());
            }
        }
        return shippingAddress;
    }

}

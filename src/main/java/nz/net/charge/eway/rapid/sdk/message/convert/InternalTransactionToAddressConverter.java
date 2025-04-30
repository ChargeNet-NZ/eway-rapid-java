package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.internal.ShippingAddress;
import nz.net.charge.eway.rapid.sdk.beans.internal.Transaction;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;

public class InternalTransactionToAddressConverter implements BeanConverter<Transaction, Address> {

    public Address doConvert(Transaction iTransaction) throws RapidSdkException {
        Address address = new Address();
        ShippingAddress shippingAddress = iTransaction.getShippingAddress();
        if (shippingAddress != null) {
            address.setState(shippingAddress.getState());
            address.setStreet1(shippingAddress.getStreet1());
            address.setPostalCode(shippingAddress.getPostalCode());
            address.setCity(shippingAddress.getCity());
            address.setCountry(shippingAddress.getCountry());
            address.setStreet2(shippingAddress.getStreet2());
        }
        return address;
    }

}

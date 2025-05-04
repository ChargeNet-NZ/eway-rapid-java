package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.ShippingDetails;
import nz.net.charge.eway.rapid.sdk.beans.internal.ShippingAddress;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;

public class ShippingDetailsToAddressConverter implements BeanConverter<ShippingDetails, ShippingAddress> {

    public ShippingAddress doConvert(ShippingDetails detail) throws RapidSdkException {
        ShippingAddress address = new ShippingAddress();
        if (detail != null) {
            Address add = detail.getShippingAddress();
            if (add != null) {
                address.setCity(add.getCity());
                address.setCountry(add.getCountry());
                address.setState(add.getState());
                address.setStreet1(add.getStreet1());
                address.setStreet2(add.getStreet2());
                address.setPostalCode(add.getPostalCode());
            }
            address.setFirstName(detail.getFirstName());
            address.setLastName(detail.getLastName());
            address.setPhone(detail.getPhone());
            address.setFax(detail.getFax());
            address.setShippingMethod(detail.getShippinhgMethod() != null ? detail.getShippinhgMethod().name() : "");
        }
        return address;
    }

}

package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;

public class InternalCustomerToCustomerConverter
        implements
        BeanConverter<nz.net.charge.eway.rapid.sdk.beans.internal.Customer, Customer> {

    public Customer doConvert(
            nz.net.charge.eway.rapid.sdk.beans.internal.Customer iCustomer) {
        Customer customer = new Customer();
        if (iCustomer != null) {
            customer.setComments(iCustomer.getComments());
            customer.setTokenCustomerID(iCustomer.getTokenCustomerID());
            customer.setMobile(iCustomer.getMobile());
            customer.setPhone(iCustomer.getPhone());
            customer.setTitle(iCustomer.getTitle());
            customer.setCompanyName(iCustomer.getCompanyName());
            customer.setFax(iCustomer.getFax());
            customer.setFirstName(iCustomer.getFirstName());
            customer.setLastName(iCustomer.getLastName());
            customer.setJobDescription(iCustomer.getJobDescription());
            customer.setReference(iCustomer.getReference());
            customer.setUrl(iCustomer.getUrl());
            customer.setCustomerDeviceIP(iCustomer.getCustomerDeviceIP());
            customer.setEmail(iCustomer.getEmail());
            
            Address address = new Address();
            address.setCity(iCustomer.getCity());
            address.setStreet1(iCustomer.getStreet1());
            address.setStreet2(iCustomer.getStreet2());
            address.setPostalCode(iCustomer.getPostalCode());
            address.setCountry(iCustomer.getCountry());
            address.setState(iCustomer.getState());
            customer.setAddress(address);
            customer.setCardDetails(iCustomer.getCardDetails());
        }
        return customer;
    }

}

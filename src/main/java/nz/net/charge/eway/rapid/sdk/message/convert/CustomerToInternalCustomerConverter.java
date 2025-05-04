package nz.net.charge.eway.rapid.sdk.message.convert;

import nz.net.charge.eway.rapid.sdk.beans.external.Address;
import nz.net.charge.eway.rapid.sdk.beans.external.CardDetails;
import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.exception.ParameterInvalidException;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import org.apache.commons.lang3.StringUtils;

public class CustomerToInternalCustomerConverter implements BeanConverter<Customer, nz.net.charge.eway.rapid.sdk.beans.internal.Customer> {

    private boolean mergeCardDetailToThis = true;

    public CustomerToInternalCustomerConverter() {}

    public CustomerToInternalCustomerConverter(boolean isMergeCardInfo) {
        this.mergeCardDetailToThis = isMergeCardInfo;
    }

    public nz.net.charge.eway.rapid.sdk.beans.internal.Customer doConvert(Customer customer) throws RapidSdkException {
        nz.net.charge.eway.rapid.sdk.beans.internal.Customer iCustomer = new nz.net.charge.eway.rapid.sdk.beans.internal.Customer();
        if (customer != null) {
            iCustomer.setPhone(customer.getPhone());
            iCustomer.setReference(customer.getReference());
            iCustomer.setTokenCustomerID(customer.getTokenCustomerID());
            iCustomer.setComments(customer.getComments());
            iCustomer.setMobile(customer.getMobile());
            iCustomer.setTitle(customer.getTitle());
            iCustomer.setFax(customer.getFax());
            iCustomer.setCustomerDeviceIP(customer.getCustomerDeviceIP());
            iCustomer.setEmail(customer.getEmail());
            iCustomer.setCompanyName(customer.getCompanyName());
            iCustomer.setUrl(customer.getUrl());
            iCustomer.setFirstName(customer.getFirstName());
            iCustomer.setLastName(customer.getLastName());
            iCustomer.setJobDescription(customer.getJobDescription());

            // Card detail
            CardDetails cardDetail = customer.getCardDetails();
            if (cardDetail == null) {
                cardDetail = new CardDetails();
            }
            iCustomer.setCardDetails(cardDetail);
            if (cardDetail != null && this.mergeCardDetailToThis) {
                iCustomer.setCardStartMonth(cardDetail.getStartMonth());
                iCustomer.setCardStartYear(cardDetail.getStartYear());
                iCustomer.setCardIssueNumber(cardDetail.getIssueNumber());
                iCustomer.setCardName(cardDetail.getName());
                iCustomer.setCardNumber(cardDetail.getNumber());
                if (!StringUtils.isBlank(cardDetail.getExpiryYear())) {
                    try {
                        iCustomer.setCardExpiryYear(Integer.parseInt(cardDetail.getExpiryYear()));
                    } catch (RuntimeException e) {
                        throw new ParameterInvalidException("Invalid expiry year in card details");
                    }
                }

                if (!StringUtils.isBlank(cardDetail.getExpiryMonth())) {
                    try {
                        iCustomer.setCardExpiryMonth(Integer.parseInt(cardDetail.getExpiryMonth()));
                    } catch (RuntimeException e) {
                        throw new ParameterInvalidException("Invalid expiry month in card details");
                    }
                }
            }

            Address cusAddress = customer.getAddress();
            if (cusAddress != null) {
                iCustomer.setState(cusAddress.getState());
                iCustomer.setPostalCode(cusAddress.getPostalCode());
                iCustomer.setCountry(cusAddress.getCountry());
                iCustomer.setCity(cusAddress.getCity());
                iCustomer.setStreet1(cusAddress.getStreet1());
                iCustomer.setStreet2(cusAddress.getStreet2());
            }
        }

        return iCustomer;
    }
}

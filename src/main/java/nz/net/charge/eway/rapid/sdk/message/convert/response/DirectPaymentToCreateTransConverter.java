package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.beans.external.Customer;
import nz.net.charge.eway.rapid.sdk.beans.external.Transaction;
import nz.net.charge.eway.rapid.sdk.beans.external.TransactionType;
import nz.net.charge.eway.rapid.sdk.entities.DirectPaymentResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.DirectPaymentToTransStatusConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalCustomerToCustomerConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.PaymentToPaymentDetailsConverter;
import nz.net.charge.eway.rapid.sdk.output.CreateTransactionResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class DirectPaymentToCreateTransConverter implements BeanConverter<DirectPaymentResponse, CreateTransactionResponse> {

    public CreateTransactionResponse doConvert(DirectPaymentResponse response) throws RapidSdkException {
        CreateTransactionResponse transactionResponse = new CreateTransactionResponse();
        Transaction transaction = new Transaction();
        String type = response.getTransactionType();
        for (TransactionType t : TransactionType.values()) {
            if (t.toString().equalsIgnoreCase(type)) {
                transaction.setTransactionType(t);
                break;
            }
        }

        PaymentToPaymentDetailsConverter paymentConverter = new PaymentToPaymentDetailsConverter();
        transaction.setPaymentDetails(paymentConverter.doConvert(response.getPayment()));

        BeanConverter<nz.net.charge.eway.rapid.sdk.beans.internal.Customer, Customer> converter = new InternalCustomerToCustomerConverter();
        transaction.setCustomer(converter.doConvert(response.getCustomer()));

        transactionResponse.setTransaction(transaction);
        if (!StringUtils.isBlank(response.getErrors())) {
            transactionResponse.setErrors(Arrays.asList(response.getErrors().split("\\s*,\\s*")));
        }

        DirectPaymentToTransStatusConverter statusConvert = new DirectPaymentToTransStatusConverter();
        transactionResponse.setTransactionStatus(statusConvert.doConvert(response));

        transactionResponse.setAccessCode(transactionResponse.getAccessCode());

        return transactionResponse;
    }

}

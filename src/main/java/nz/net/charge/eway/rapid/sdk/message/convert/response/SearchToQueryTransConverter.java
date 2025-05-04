package nz.net.charge.eway.rapid.sdk.message.convert.response;

import nz.net.charge.eway.rapid.sdk.beans.external.TransactionStatus;
import nz.net.charge.eway.rapid.sdk.beans.internal.Transaction;
import nz.net.charge.eway.rapid.sdk.entities.TransactionSearchResponse;
import nz.net.charge.eway.rapid.sdk.exception.RapidSdkException;
import nz.net.charge.eway.rapid.sdk.message.convert.BeanConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalTransToTransConverter;
import nz.net.charge.eway.rapid.sdk.message.convert.InternalTransactionToStatusConverter;
import nz.net.charge.eway.rapid.sdk.output.QueryTransactionResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class SearchToQueryTransConverter implements BeanConverter<TransactionSearchResponse, QueryTransactionResponse> {

    public QueryTransactionResponse doConvert(TransactionSearchResponse response) throws RapidSdkException {
        QueryTransactionResponse queryTransactionResponse = new QueryTransactionResponse();
        if (response.getTransactions() != null && response.getTransactions().length > 0) {
            BeanConverter<Transaction, nz.net.charge.eway.rapid.sdk.beans.external.Transaction> transConverter = new InternalTransToTransConverter();
            queryTransactionResponse.setTransaction(transConverter.doConvert(response.getTransactions()[0]));
        }
        if (!StringUtils.isBlank(response.getError())) {
            queryTransactionResponse.setErrors(Arrays.asList(response.getError().split("\\s*,\\s*")));
        }
        BeanConverter<Transaction, TransactionStatus> statusConvert = new InternalTransactionToStatusConverter();

        if (response.getTransactions() != null && response.getTransactions().length > 0) {
            queryTransactionResponse.setTransactionStatus(statusConvert.doConvert(response.getTransactions()[0]));
        }
        return queryTransactionResponse;
    }

}

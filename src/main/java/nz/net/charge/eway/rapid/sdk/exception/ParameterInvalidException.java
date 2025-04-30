package nz.net.charge.eway.rapid.sdk.exception;

import nz.net.charge.eway.rapid.sdk.util.Constant;

import java.io.Serial;

/**
 * Define invalid parameter exception
 *
 */
public class ParameterInvalidException extends RapidSdkException {

    @Serial
    private static final long serialVersionUID = 8156273343136137656L;

    public ParameterInvalidException(String message) {
        super(Constant.INTERNAL_RAPID_API_ERROR_CODE, message);
    }

    public ParameterInvalidException(String message, Throwable t) {
        super(Constant.INTERNAL_RAPID_API_ERROR_CODE, message, t);
    }

}

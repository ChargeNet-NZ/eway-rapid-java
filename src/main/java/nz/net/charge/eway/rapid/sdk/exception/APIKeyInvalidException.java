package nz.net.charge.eway.rapid.sdk.exception;

import nz.net.charge.eway.rapid.sdk.util.Constant;

import java.io.Serial;

/**
 * API Key invalid exception
 */
public class APIKeyInvalidException extends RapidSdkException {

    @Serial
    private static final long serialVersionUID = 550808787239714509L;

    public APIKeyInvalidException(String message) {
        super(Constant.API_KEY_INVALID_ERROR_CODE, message);
    }

}

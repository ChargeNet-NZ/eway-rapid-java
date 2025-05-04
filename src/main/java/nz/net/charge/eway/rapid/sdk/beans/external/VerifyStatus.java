package nz.net.charge.eway.rapid.sdk.beans.external;

/**
 * Possible values returned from the payment providers with regards to
 * verification of card/user details
 */
public enum VerifyStatus {

    Unchecked("unchecked", 0),
    Valid("valid", 1),
    Invalid("invalid", 2);

    private final int code;
    private final String name;

    VerifyStatus(final String name, final int code) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

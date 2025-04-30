package nz.net.charge.eway.rapid.sdk.integration;

import nz.net.charge.eway.rapid.sdk.RapidClient;
import nz.net.charge.eway.rapid.sdk.RapidSDK;

public class IntegrationTest {

    protected final String PASSWORD = "API-P4ss";
    protected final String APIKEY = "60CF3Ce97nRS1Z1Wp5m9kMmzHHEh8Rkuj31QCtVxjPWGYA9FymyqsK0Enm1P6mHJf0THbR";
    protected final String SANDBOX_ENDPOINT = "https://api.sandbox.ewaypayments.com";
    private RapidClient client;

    public RapidClient getSandboxClient() {
        client = RapidSDK.newRapidClient(APIKEY, PASSWORD, SANDBOX_ENDPOINT);
        if (System.getenv("APIVERSION") != null) {
            client.setVersion(System.getenv("APIVERSION"));
        }
        return client;
    }

}

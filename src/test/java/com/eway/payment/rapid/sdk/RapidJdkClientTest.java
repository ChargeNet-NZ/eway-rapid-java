package com.eway.payment.rapid.sdk;

import com.eway.payment.rapid.sdk.util.Constant;
import com.eway.payment.rapid.sdk.util.ResourceUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class RapidJdkClientTest {

    private RapidClient client;

    @Test
    public void testValidRapidClientInputParam() {
        String APIKey = "skjskj";
        String passWord = "uncover";
        String endpoint = "https://api.sandbox.ewaypayments.com";
        client = RapidSDK.newRapidClient(APIKey, passWord, endpoint);
        List<String> listError = client.getErrors();
        assertThat(client.isValid()).isTrue();
        assertThat(listError).isEmpty();
    }

    @Test
    public void testInvalidRapidEndpoint() {
        String APIKey = "skjskj";
        String passWord = "uncover";
        String endpoint = "";
        client = RapidSDK.newRapidClient(APIKey, passWord, endpoint);
        List<String> listError = client.getErrors();
        assertThat(client.isValid()).isFalse();
        assertThat(listError).hasSize(1);
        for (String err : listError) {
            assertThat(err).isEqualToIgnoringCase(Constant.LIBRARY_NOT_HAVE_ENDPOINT_ERROR_CODE);
        }

    }

    @Test
    public void testMissingApiKeyOrPassword() {
        String APIKey = "skjskj";
        String passWord = "";
        String endpoint = "htttp://";
        client = RapidSDK.newRapidClient(APIKey, passWord, endpoint);
        List<String> listError = client.getErrors();
        assertThat(client.isValid()).isFalse();
        assertThat(listError).hasSize(1);
        for (String err : listError) {
            assertThat(err).isEqualToIgnoringCase(Constant.API_KEY_INVALID_ERROR_CODE);
        }
    }

    @Test
    public void testMissingApiKeyAndRapidEndpoint() {
        String APIKey = "skjskj";
        String passWord = "";
        String endpoint = "";
        client = RapidSDK.newRapidClient(APIKey, passWord, endpoint);
        List<String> listError = client.getErrors();
        assertThat(client.isValid()).isFalse();
        assertThat(listError).hasSize(2);
        for (String err : listError) {
            assertThat(err).isEqualToIgnoringCase(Constant.LIBRARY_NOT_HAVE_ENDPOINT_ERROR_CODE);
        }
        assertThat(listError).contains(Constant.API_KEY_INVALID_ERROR_CODE);
    }

    @Test
    public void testInvalidSetCredential() {
        String APIKey = "skjskj";
        String passWord = "uncover";
        String endpoint = "https://api.sandbox.ewaypayments.com";
        client = RapidSDK.newRapidClient(APIKey, passWord, endpoint);
        assertThat(client.isValid()).isTrue();
        String newAPIKey = "";
        String newPass = "uncover";
        client.setCredentials(newAPIKey, newPass);
        List<String> listError = client.getErrors();
        assertThat(client.isValid()).isFalse();
        assertThat(listError).hasSize(1);
        for (String err : listError) {
            assertThat(err).isEqualToIgnoringCase(Constant.API_KEY_INVALID_ERROR_CODE);
        }
    }

    @Test
    public void testInvalidWhenInstallClientButValidSetCredential() {
        String APIKey = "skjskj";
        String passWord = "";
        String endpoint = "https://api.sandbox.ewaypayments.com";
        client = RapidSDK.newRapidClient(APIKey, passWord, endpoint);
        assertThat(client.isValid()).isFalse();
        assertThat(client.getErrors()).hasSize(1);
        String newAPIKey = "newAPIKey";
        String newPass = "uncover";
        client.setCredentials(newAPIKey, newPass);
        assertThat(client.isValid()).isTrue();
        assertThat(client.getErrors()).isEmpty();
    }

    @Test
    public void testInitClientWithRapidEndpointIsSandboxOrProduction()
            throws Exception {
        String APIKey = "skjskj";
        String passWord = "jjhhjk";
        String endpoint = "sandbox";
        client = RapidSDK.newRapidClient(APIKey, passWord, endpoint);
        Properties prop = ResourceUtil
                .loadProperies(Constant.RAPID_API_RESOURCE);
        Class<?> c = RapidClientImpl.class;
        Field field = c.getDeclaredField("webUrl");
        field.setAccessible(true);
        String value = (String) field.get(client);
        assertThat(value).isEqualToIgnoringCase(prop
                .getProperty(Constant.GLOBAL_RAPID_SANDBOX_REST_URL_PARAM));
        // Load properties file
    }

    @Test
    public void testFindErrorCode() {
        String errCode = "S9991";
        assertThat(RapidSDK.userDisplayMessage(errCode, "en"))
                .isEqualTo("Library does not have an API key or password initialised, or are invalid");
    }

    @Test
    public void testNullErrorCode() {
        String errCode = null;
        assertThat(RapidSDK.userDisplayMessage(errCode, "en")).isNull();
    }
}

package org.kubis.mastercard;

import com.mastercard.developer.utils.AuthenticationUtils;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.security.PrivateKey;

/***
 * Class responsible for basing credential handling for MasterCard Api
 */
@ApplicationScoped
public class MasterCardCredentials {

    @ConfigProperty(name = "mastercard.consumer-key")
    String consumerKey;

    @ConfigProperty(name = "mastercard.keystore.path")
    String keyStorePath;

    @ConfigProperty(name = "mastercard.keystore.alias")
    String keystoreAlias;

    @ConfigProperty(name = "mastercard.keystore.password")
    String keystorePassword;

    private PrivateKey signingKey;

    @PostConstruct
    void loadKey() {
        try {
            signingKey = AuthenticationUtils.loadSigningKey(keyStorePath, keystoreAlias, keystorePassword);
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot load MasterCard signing key");
        }
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public PrivateKey getSigningKey() {
        return signingKey;
    }
}

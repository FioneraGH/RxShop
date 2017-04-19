package com.centling.http;

import com.centling.BaseApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * SSLHelper
 * Created by fionera on 17-3-31 in sweeping_robot.
 */

class SSLHelper {
    private final static String CLIENT_PRI_KEY = "client.cer";
    private final static String TRUST_STORE_PUB_KEY = "trust_store.bks";
    private final static String CLIENT_BKS_PASSWORD = "tt.123";
    private final static String TRUST_STORE_BKS_PASSWORD = "tt.123";

    static SSLSocketFactory getCertification() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream ksIn = BaseApplication.getInstance().getAssets().open(CLIENT_PRI_KEY);
            keyStore.load(ksIn, CLIENT_BKS_PASSWORD.toCharArray());
            ksIn.close();

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, CLIENT_BKS_PASSWORD.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = getTrustManager();
            if (trustManagerFactory != null) {
                sslContext.init(keyManagerFactory.getKeyManagers(),
                        trustManagerFactory.getTrustManagers(), null);
            }
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                CertificateException | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    private static TrustManagerFactory trustManagerFactory;

    static TrustManagerFactory getTrustManager() {
        if (trustManagerFactory != null) {
            return trustManagerFactory;
        }
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream tsIn = BaseApplication.getInstance().getAssets().open(TRUST_STORE_PUB_KEY);
            trustStore.load(tsIn, TRUST_STORE_BKS_PASSWORD.toCharArray());
            tsIn.close();
            trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trustManagerFactory;
    }
}

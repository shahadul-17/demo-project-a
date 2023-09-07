package com.demoproject.net;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * This class ignores X509Certificate validation.
 * @implNote This insecure trust manager is used to make sure that
 * we are able to connect to HTTP servers with expired SSL certificates.
 */
public class InsecureTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] certificates,
                                   String str) throws CertificateException { }

    @Override
    public void checkServerTrusted(X509Certificate[] certificates,
                                   String str) throws CertificateException { }

    @Override
    public X509Certificate[] getAcceptedIssuers() { return null; }
}
